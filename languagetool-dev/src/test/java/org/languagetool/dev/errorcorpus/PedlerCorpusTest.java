/* LanguageTool, a natural language style checker 
 * Copyright (C) 2014 Daniel Naber (http://www.danielnaber.de)
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
package org.languagetool.dev.errorcorpus;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PedlerCorpusTest {

  @Test
  public void testCorpusAccess() throws IOException {
    URL resource = PedlerCorpusTest.class.getResource("/org/languagetool/dev/eval/error-corpus-example.txt");
    assertNotNull("Missing test resource: /org/languagetool/dev/eval/error-corpus-example.txt", resource);

    File errorFile = new File(resource.getFile());
    PedlerCorpus corpus = new PedlerCorpus(errorFile.getParentFile());
    Iterator<ErrorSentence> iterator = corpus.iterator();
    assertTrue(iterator.hasNext());
    ErrorSentence sentence1 = iterator.next();
    assertEquals("But also please not that grammar checkers aren't perfect.",
      sentence1.getAnnotatedText().getPlainText());
    assertEquals("But <ERR targ=foo>also</ERR> please <ERR targ=note>not</ERR> that grammar checkers aren't perfect.",
      sentence1.getMarkupText());

    ErrorSentence sentence2 = iterator.next();
    assertEquals("But also also please note note that grammar checkers aren't perfect.",
      sentence2.getAnnotatedText().getPlainText());
    assertEquals("But <ERR targ=bad suggestion>also also</ERR> please <ERR targ=note>note note</ERR> that grammar checkers aren't perfect.",
      sentence2.getMarkupText());

    assertTrue(iterator.hasNext());
    iterator.next();
    assertFalse(iterator.hasNext());
  } 

}
