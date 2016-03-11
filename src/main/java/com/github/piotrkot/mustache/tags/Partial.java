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

import com.github.piotrkot.mustache.Tag;
import com.github.piotrkot.mustache.TagIndicate;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Partial tag type. Renders text at runtime based on file injection. Recursion
 * is possible so avoid infinite loops.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class Partial implements Tag {
    /**
     * Indicate.
     */
    private final transient TagIndicate indct;
    /**
     * Constructor.
     * @param indicate Indicate.
     */
    public Partial(final TagIndicate indicate) {
        this.indct = indicate;
    }

    @Override
    public String render(final String tmpl, final Map<String, Object> pairs) {
        final StringBuilder result = new StringBuilder();
        int start = 0;
        final Matcher matcher = Pattern.compile(
            new StringJoiner(
                this.indct.start(),
                "\\s+>\\s+(\\S+)\\s+",
                this.indct.end()
            ).toString()
        ).matcher(tmpl);
        while (matcher.find()) {
            result.append(tmpl.substring(start, matcher.start()));
            final String file = matcher.group(1);
            if (pairs.containsKey(file)) {
                result.append(file);
            }
            start = matcher.end();
        }
        result.append(tmpl.substring(start, tmpl.length()));
        return result.toString();
    }
}
