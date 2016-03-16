/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 piotrkot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.piotrkot.mustache;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Match which can use function applied on input for pattern search. This is
 * to simplify regex creation.
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class EasyMatch {
    /**
     * Matcher.
     */
    private final Matcher mtchr;
    /**
     * Input against the pattern.
     */
    private final CharSequence inpt;
    /**
     * Function applied on inpt.
     */
    private final Function<Context, Boolean> funct;

    /**
     * Constructor.
     * @param pattern Pattern.
     * @param input Input matched against the pattern.
     * @param function Function applied on inpt.
     */
    public EasyMatch(final Pattern pattern, final CharSequence input,
        final Function<Context, Boolean> function) {
        this.mtchr = pattern.matcher(input);
        this.inpt = input;
        this.funct = function;
    }

    /**
     * Finds the next subsequence of inpt that matches the pattern. Just
     * like {@link Matcher#find()} does. And is checks appliance of the
     * function.
     * @return True if subsequence matches this pattern.
     * @see Matcher#find()
     */
    public boolean find() {
        return this.mtchr.find()
            && this.funct.apply(new Context(this.inpt, this.mtchr));
    }

    /**
     * Start index of previous match, like {@link Matcher#start()}.
     * @return Index of first char matched.
     * @see Matcher#start()
     */
    public int start() {
        return this.mtchr.start();
    }

    /**
     * Offset after last char matched, like {@link Matcher#end()}.
     * @return Offset after last char matched.
     * @see Matcher#end()
     */
    public int end() {
        return this.mtchr.end();
    }

    /**
     * Input subsequence captured by the given group during the previous
     * match, like {@link Matcher#group(int)}.
     * @param group Index of group.
     * @return Subsequence captured by the given group during the previous
     *  match.
     * @see Matcher#group(int)
     */
    public String group(final int group) {
        return this.mtchr.group(group);
    }

    /**
     * Context of the match.
     */
    public final class Context {
        /**
         * Input sequence.
         */
        private final CharSequence inpt;
        /**
         * Match of the pattern.
         */
        private final Matcher mtch;

        /**
         * Constructor.
         * @param input Input sequence.
         * @param match Match of the pattern.
         */
        public Context(final CharSequence input, final Matcher match) {
            this.inpt = input;
            this.mtch = match;
        }

        /**
         * Input string.
         * @return String.
         */
        public String input() {
            return this.inpt.toString();
        }

        /**
         * Start of the pattern match.
         * @return Start index of the match.
         */
        public int start() {
            return this.mtch.start();
        }

        /**
         * End of the pattern match.
         * @return End index of the match.
         */
        public int end() {
            return this.mtch.end();
        }
    }
}
