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

import org.apache.lucene.analysis.hunspell.Dictionary;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LuceneHunspellDictionary implements HunspellDictionary {
  private final org.apache.lucene.analysis.hunspell.Hunspell hunspell;
  private boolean closed = false;
  private final InputStream dictInputStream;
  private final InputStream affixInputStream;
  private final Set<String> customWords;

  public LuceneHunspellDictionary(Path dictPath, Path affixPath) {
    try {
      Path dirTmp = Files.createTempDirectory("languagetool-lucene");
      Directory tmpDirectory = new SimpleFSDirectory(dirTmp);
      dictInputStream = Files.newInputStream(dictPath);
      affixInputStream = Files.newInputStream(affixPath);
      Dictionary dictionary = new Dictionary(tmpDirectory, "languagetool", dictInputStream, affixInputStream);
      hunspell = new org.apache.lucene.analysis.hunspell.Hunspell(dictionary);
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
    List<String> suggestions = new ArrayList<>(hunspell.suggest(word));

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
          lowerWord.startsWith(customWord.substring(0, Math.min(2, customWord.length()))))) {
        if (!suggestions.contains(customWord)) {
          suggestions.add(customWord);
        }
      }
    }
  }

  @Override
  public boolean isClosed() {
    return closed;
  }

  @Override
  public void close() throws IOException {
    if (dictInputStream != null) {
      dictInputStream.close();
    }
    if (affixInputStream != null) {
      affixInputStream.close();
    }
    customWords.clear();
    closed = true;
  }
}
