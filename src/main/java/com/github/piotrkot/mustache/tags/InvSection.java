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
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Inverted section tag type. Renders text once but in condition inverted to
 * section condition.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class InvSection implements Tag {
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
     *
     * @param indicate Indicate.
     */
    public InvSection(final TagIndicate indicate) {
        this.vrble = new Variable(indicate);
        // @checkstyle LineLength (3 lines)
        this.patt = Pattern.compile(
            String.format(
                "%1$s\\s*\\^\\s*([\\w\\.]+)\\s*%2$s(.+?)%1$s\\s*/\\s*(\\1)\\s*%2$s",
                indicate.safeStart(),
                indicate.safeEnd()
            ),
            Pattern.DOTALL
        );
    }
    @Override
    public String render(final CharSequence tmpl,
        final Map<CharSequence, Object> pairs) {
        final StringBuilder result = new StringBuilder();
        int start = 0;
        final Matcher matcher = this.patt.matcher(tmpl);
        while (matcher.find()) {
            result.append(tmpl.subSequence(start, matcher.start()));
            final String name = matcher.group(1);
            final Object value = pairs.get(name);
            if (pairs.containsKey(name)
                && value.toString().equals(Boolean.FALSE.toString())) {
                result.append(this.vrble.render(matcher.group(2), pairs));
            } else if (pairs.containsKey(name) && value instanceof Collection
                && ((Collection) value).isEmpty()) {
                result.append(this.vrble.render(matcher.group(2), pairs));
            }
            start = matcher.end();
        }
        result.append(tmpl.subSequence(start, tmpl.length()));
        String out = result.toString();
        if (this.patt.matcher(out).find()) {
            out = this.render(out, pairs);
        }
        return out;
    }
}
