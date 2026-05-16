/* LanguageTool, a natural language style checker 
 * Copyright (C) 2005 Daniel Naber (http://www.danielnaber.de)
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
package org.languagetool.tokenizers.ja;

import java.util.ArrayList;
import java.util.List;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

public class JapaneseWordTokenizer implements org.languagetool.tokenizers.Tokenizer {

  private final Tokenizer tokenizer;

  public JapaneseWordTokenizer() {
    tokenizer = new Tokenizer.Builder().build();
  }

  @Override  
  public List<String> tokenize(String text) {
    List<String> ret = new ArrayList<>();
    try {
      List<Token> tokens = tokenizer.tokenize(text);
      for (Token token : tokens) {
        String basicForm;
        if (token.getMorpheme().getBasicForm().equalsIgnoreCase("*")) {
          basicForm = token.getSurface();
        } else {
          basicForm = token.getMorpheme().getBasicForm();
        }
        String pos = token.getPartOfSpeechLevel1();

        ret.add(token.getSurface() + " " + pos + " " + basicForm);
      }
    } catch (Exception e) {
      return ret;
    }
    return ret;
  }
}
