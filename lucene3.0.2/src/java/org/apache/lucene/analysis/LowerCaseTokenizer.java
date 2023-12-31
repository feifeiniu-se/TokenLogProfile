package org.apache.lucene.analysis;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, loggenerate
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Reader;

import org.apache.lucene.util.AttributeSource;

/**
 * LowerCaseTokenizer performs the function of LetterTokenizer
 * and LowerCaseFilter together.  It divides text at non-letters and converts
 * them to lower case.  While it is functionally equivalent to the combination
 * of LetterTokenizer and LowerCaseFilter, there is a performance advantage
 * to doing the two tasks at once, hence this (redundant) implementation.
 * <P>
 * Note: this does a decent job for most European languages, but does a terrible
 * job for some Asian languages, where words are not separated by spaces.
 */
public final class LowerCaseTokenizer extends LetterTokenizer {
  /** Construct a new LowerCaseTokenizer. */
  public LowerCaseTokenizer(Reader in) {
    super(in);
  }

  /** Construct a new LowerCaseTokenizer using a given {@link AttributeSource}. */
  public LowerCaseTokenizer(AttributeSource source, Reader in) {
    super(source, in);
  }

  /** Construct a new LowerCaseTokenizer using a given {@link org.apache.lucene.util.AttributeSource.AttributeFactory}. */
  public LowerCaseTokenizer(AttributeFactory factory, Reader in) {
    super(factory, in);
  }
  
  /** Converts char to lower case
   * {@link Character#toLowerCase(char)}.*/
  @Override
  protected char normalize(char c) {
    return Character.toLowerCase(c);
  }
}
