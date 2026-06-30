/* LanguageTool, a natural language style checker
 * Copyright (C) 2025 Hiroshi Miura
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package org.languagetool.rules.spelling.hunspell;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.hunspell.Dictionary;
import org.apache.lucene.analysis.hunspell.TimeoutPolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LuceneHunspellDictionary implements HunspellDictionary {

  /**
   * Default per-call suggestion time budget used when the running Lucene version
   * exposes {@code Hunspell#setSuggestionTimeLimit(int)}. 60s is generous on
   * purpose: under load on shared CI machines the suggester can easily take
   * several seconds for the first call after JVM warm-up.
   */
  private static final int DEFAULT_SUGGEST_TIME_LIMIT_MS = 1_000;

  private final org.apache.lucene.analysis.hunspell.Hunspell hunspell;
  private final int suggestTimeLimitMs;
  private final InputStream dictInputStream;
  private final InputStream affixInputStream;
  private final Set<String> customWords;
  private final Path dictionaryPath;
  private final Path affixPath;
  @Getter
  private final boolean deleteOnClose;
  @Getter
  private boolean closed = false;

  public LuceneHunspellDictionary(Path dictPath, Path affixPath, boolean cleanup) {
    this(dictPath, affixPath, cleanup, TimeoutPolicy.RETURN_PARTIAL_RESULT, DEFAULT_SUGGEST_TIME_LIMIT_MS);
  }

  public LuceneHunspellDictionary(Path dictPath, Path affixPath, boolean cleanup, TimeoutPolicy timeoutPolicy) {
    this(dictPath, affixPath, cleanup, timeoutPolicy, DEFAULT_SUGGEST_TIME_LIMIT_MS);
  }

  /**
   * @param dictPath            path to the {@code .dic} file
   * @param affixPath           path to the {@code .aff} file
   * @param cleanup             delete dict/aff on close
   * @param timeoutPolicy       policy applied by Lucene when {@code suggestTimeLimitMs}
   *                            is exceeded. Tests that need deterministic
   *                            suggestion content should use
   *                            {@link TimeoutPolicy#NO_TIMEOUT}.
   * @param suggestTimeLimitMs  per-call wall-clock budget passed to
   *                            {@code Hunspell#suggest(String, long)}.
   *                            Ignored when {@code timeoutPolicy} is
   *                            {@link TimeoutPolicy#NO_TIMEOUT}.
   */
  public LuceneHunspellDictionary(Path dictPath, Path affixPath, boolean cleanup,
                                  TimeoutPolicy timeoutPolicy, int suggestTimeLimitMs) {
    if (suggestTimeLimitMs < 0) {
      throw new IllegalArgumentException("suggestTimeLimitMs must be non-negative");
    }
    this.dictionaryPath = dictPath;
    this.affixPath = affixPath;
    this.deleteOnClose = cleanup;
    this.suggestTimeLimitMs = suggestTimeLimitMs;
    try {
      Path dirTmp = Files.createTempDirectory("languagetool-lucene");
      Directory tmpDirectory = FSDirectory.open(dirTmp);
      dictInputStream = Files.newInputStream(dictPath);
      affixInputStream = Files.newInputStream(affixPath);
      Dictionary dictionary = new Dictionary(tmpDirectory, "languagetool", affixInputStream,
        Collections.singletonList(dictInputStream), false);
      hunspell = new org.apache.lucene.analysis.hunspell.Hunspell(dictionary, timeoutPolicy, () -> {});
      customWords = ConcurrentHashMap.newKeySet();
    } catch (IOException | ParseException e) {
      throw new RuntimeException("Could not create Hunspell dictionary instance.", e);
    }
  }

  @Override
  public boolean spell(String word) {
    if (closed) {
      throw new RuntimeException("Attempt to use hunspell instance after closing");
    }
    return hunspell.spell(word);
  }

  @Override
  public void add(String word) {
    if (closed) {
      throw new RuntimeException("Attempt to use hunspell instance after closing");
    }

    if (word != null && !word.trim().isEmpty()) {
      // Store in lowercase for consistent lookup
      customWords.add(word.toLowerCase());

      // Optionally also store the original case
      if (!word.equals(word.toLowerCase())) {
        customWords.add(word);
      }
    }
  }

  @Override
  public List<String> suggest(String word) {
    if (closed) {
      throw new RuntimeException("Attempt to use hunspell instance after closing");
    }

    // If the word is already correct (including custom words), return empty suggestions
    if (spell(word)) {
      return Collections.emptyList();
    }

    // Get suggestions from Lucene Hunspell
    List<String> suggestions = new ArrayList<>(hunspell.suggest(word, suggestTimeLimitMs));

    // Optionally enhance suggestions with similar custom words
    enhanceSuggestionsWithCustomWords(word, suggestions);

    return Collections.unmodifiableList(suggestions);
  }

  /**
   * Enhance suggestions by adding similar custom words
   */
  private void enhanceSuggestionsWithCustomWords(String word, List<String> suggestions) {
    String lowerWord = word.toLowerCase();

    // Add custom words that are similar (simple similarity check)
    for (String customWord : customWords) {
      if (customWord.length() > 2 &&
        (customWord.startsWith(lowerWord.substring(0, Math.min(2, lowerWord.length()))) ||
          lowerWord.startsWith(customWord.substring(0, 2 /* Math.min(2, customWordWord.length()) is always 2 */)))) {
        if (!suggestions.contains(customWord)) {
          suggestions.add(customWord);
        }
      }
    }
  }

  @Override
  public void close() throws IOException {
    closed = true;
    if (dictInputStream != null) {
      dictInputStream.close();
    }
    if (affixInputStream != null) {
      affixInputStream.close();
    }
    customWords.clear();

    // Clean up temp files if this dictionary owns them (fixes #11380)
    if (deleteOnClose) {
      try {
        boolean dicDeleted = Files.deleteIfExists(dictionaryPath);
        boolean affDeleted = Files.deleteIfExists(affixPath);
        if (dicDeleted || affDeleted) {
          log.trace("Deleted temporary Hunspell files: {} (deleted: {}) and {} (deleted: {})",
            dictionaryPath, dicDeleted, affixPath, affDeleted);
        }
      } catch (IOException e) {
        // Log but don't throw - cleanup is best effort
        log.trace("Failed to delete temporary Hunspell files: {} and {}", dictionaryPath, affixPath, e);
      }
    }
  }
}
