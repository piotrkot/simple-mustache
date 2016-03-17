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
package com.github.piotrkot.mustache.tags;

import com.github.piotrkot.mustache.EasyMatch;
import com.github.piotrkot.mustache.Tag;
import com.github.piotrkot.mustache.TagIndicate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variable template tag. Basic string replacement.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class Variable implements Tag {
    /**
     * Variable pattern.
     */
    private final transient Pattern patt;
    /**
     * Indicate.
     */
    private final TagIndicate indic;

    /**
     * Constructor.
     *
     * @param indicate Indicate.
     */
    public Variable(final TagIndicate indicate) {
        this.indic = indicate;
        this.patt = Pattern.compile(
            String.format(
                "%s\\s*([\\w\\.]+)\\s*%s",
                indicate.safeStart(),
                indicate.safeEnd()
            )
        );
    }

    @Override
    public String render(final CharSequence tmpl,
        final Map<CharSequence, Object> pairs) {
        final StringBuilder result = new StringBuilder();
        int start = 0;
        final EasyMatch matcher = new EasyMatch(
            this.patt,
            tmpl,
            context -> {
                final String after = context.input().substring(context.end());
                return new PatternCount(
                    Pattern.compile(
                        String.format(
                            "%s[#\\^]\\s*[\\w\\.]+\\s*%s",
                            this.indic.safeStart(),
                            this.indic.safeEnd()
                        )
                    ).matcher(after)
                ).count() == new PatternCount(
                    Pattern.compile(
                        String.format(
                            "%s/\\s*[\\w\\.]+\\s*%s",
                            this.indic.safeStart(),
                            this.indic.safeEnd()
                        )
                    ).matcher(after)
                ).count();
            }
        );
        while (matcher.find()) {
            result.append(tmpl.subSequence(start, matcher.start()));
            final String name = matcher.group(1);
            if (pairs.containsKey(name)) {
                result.append(pairs.get(name));
            }
            start = matcher.end();
        }
        result.append(tmpl.subSequence(start, tmpl.length()));
        return result.toString();
    }

    /**
     * Count of pattern matched.
     */
    class PatternCount {
        /**
         * Matcher.
         */
        private final Matcher mtch;

        /**
         * Constructor.
         *
         * @param matcher Matcher.
         */
        PatternCount(final Matcher matcher) {
            this.mtch = matcher;
        }

        /**
         * Number of occurrences of pattern.
         *
         * @return Number of patterns found.
         */
        public int count() {
            int count = 0;
            while (this.mtch.find()) {
                count += 1;
            }
            return count;
        }
    }

}
