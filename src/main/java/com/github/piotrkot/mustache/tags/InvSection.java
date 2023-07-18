/*
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
import com.github.piotrkot.mustache.Tags;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cactoos.scalar.IoChecked;
import org.cactoos.scalar.LengthOf;

/**
 * Inverted section tag type. Renders text once but in condition inverted to
 * section condition.
 * @since 1.0
 */
public final class InvSection implements Tag {
    /**
     * Section Regexp pattern.
     */
    private final Pattern patt;

    /**
     * Nested tag Regexp pattern.
     */
    private final Pattern nest;

    /**
     * Indicate.
     */
    private final TagIndicate indic;

    /**
     * Constructor.
     * @param indicate Indicate.
     */
    public InvSection(final TagIndicate indicate) {
        this.indic = indicate;
        // @checkstyle LineLength (3 lines)
        this.patt = Pattern.compile(
            String.format(
                "%1$s\\s*\\^\\s*([\\w\\.]+)\\s*%2$s(.+?)%1$s\\s*/\\s*(\\1)\\s*%2$s",
                indicate.safeStart(),
                indicate.safeEnd()
            ),
            Pattern.DOTALL
        );
        this.nest = Pattern.compile(
            String.format(
                ".*%1$s.*%2$s.*",
                indicate.safeStart(),
                indicate.safeEnd()
            ),
            Pattern.DOTALL
        );
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    @Override
    public String render(final CharSequence tmpl,
        final Map<CharSequence, Object> pairs) throws IOException {
        final StringBuilder result = new StringBuilder();
        int start = 0;
        final Matcher matcher = this.patt.matcher(tmpl);
        while (matcher.find()) {
            result.append(tmpl.subSequence(start, matcher.start()));
            final boolean contains = pairs.containsKey(matcher.group(1));
            final String content = matcher.group(2);
            final Object value = pairs.getOrDefault(matcher.group(1), "");
            final boolean nested = this.nest.matcher(content).find();
            final boolean allowed = value instanceof Iterable
                && new IoChecked<>(new LengthOf((Iterable) value)).value() == 0
                || value.toString().equals(Boolean.FALSE.toString());
            if (contains && allowed && nested) {
                result.append(
                    new Tags(
                        new Partial(this.indic),
                        new Section(this.indic),
                        new InvSection(this.indic),
                        new Variable(this.indic)
                    ).render(matcher.group(2), pairs)
                );
            } else if (contains && allowed) {
                result.append(content);
            }
            start = matcher.end();
        }
        result.append(tmpl.subSequence(start, tmpl.length()));
        return result.toString();
    }
}
