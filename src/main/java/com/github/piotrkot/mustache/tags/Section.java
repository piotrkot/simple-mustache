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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Section tag type. Renders block of text multiple times.
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class Section implements Tag {
    /**
     * Variable tag.
     */
    private final Tag vrble;
    /**
     * Section Regexp pattern.
     */
    private final Pattern patt;

    /**
     * Constructor.
     * @param indicate Indicate.
     */
    public Section(final TagIndicate indicate) {
        this.vrble = new Variable(indicate);
        this.patt = Pattern.compile(
            String.format(
                "%1$s#([^\\s]+)%2$s(.+?)%1$s/(\\1)%2$s",
                indicate.safeStart(),
                indicate.safeEnd()
            )
        );
    }

    @Override
    public String render(final String tmpl, final Map<String, Object> pairs) {
        final StringBuilder result = new StringBuilder();
        int start = 0;
        final Matcher matcher = this.patt.matcher(tmpl);
        while (matcher.find()) {
            result.append(tmpl.substring(start, matcher.start()));
            final String name = matcher.group(1);
            final Object pair = pairs.get(name);
            final String value = matcher.group(2);
            if (pairs.containsKey(name) && pair.toString().equals("true")) {
                result.append(value);
            } else if (pairs.containsKey(name) && pair instanceof List) {
                for (final Object elem : (List) pair) {
                    if (elem instanceof Map) {
                        result.append(this.vrble.render(value, (Map) elem));
                    } else {
                        result.append(value);
                    }
                }
            }
            start = matcher.end();
        }
        result.append(tmpl.substring(start, tmpl.length()));
        String out = result.toString();
        if (this.patt.matcher(out).find()) {
            out = this.render(out, pairs);
        }
        return out;
    }
}
