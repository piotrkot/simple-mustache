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
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class Variable implements Tag {
    /**
     * Variable pattern.
     */
    private final transient Pattern patt;
    private final TagIndicate indic;

    /**
     * Constructor.
     * @param indicate Indicate.
     */
    public Variable(final TagIndicate indicate) {
        this.indic = indicate;
        this.patt = Pattern.compile(
            String.format(
                "%s([^\\s>/#\\^]+)%s",
                indicate.safeStart(),
                indicate.safeEnd()
            )
        );
    }

    @Override
    public String render(final String tmpl, final Map<String, Object> pairs) {
        final StringBuilder result = new StringBuilder();
        int start = 0;
        final EasyMatch matcher = new EasyMatch(
            this.patt,
            tmpl,
            context -> new Patt(
                Pattern.compile(
                    String.format(
                        "%s[#\\^].*?%s",
                        indic.safeStart(),
                        indic.safeEnd()
                    )
                ).matcher(
                    context.input().substring(context.end())
                )
            ).count() == new Patt(
                Pattern.compile(
                    String.format(
                        "%s/.*?%s",
                        indic.safeStart(),
                        indic.safeEnd()
                    )
                ).matcher(
                    context.input().substring(context.end())
                )
            ).count()
        );
        while (matcher.find()) {
            result.append(tmpl.substring(start, matcher.start()));
            final String name = matcher.group(1);
            if (pairs.containsKey(name)) {
                result.append(pairs.get(name));
            }
            start = matcher.end();
        }
        result.append(tmpl.substring(start, tmpl.length()));
        return result.toString();
    }

    class Patt {
        private final Matcher mtch;

        public Patt(final Matcher matcher) {
            this.mtch = matcher;
        }

        public int count() {
            int count = 0;
            while (mtch.find()) {
                count++;
            }
            return count;
        }
    }

}
