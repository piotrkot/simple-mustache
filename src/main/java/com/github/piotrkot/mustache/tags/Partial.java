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

import com.github.piotrkot.mustache.Contents;
import com.github.piotrkot.mustache.Tag;
import com.github.piotrkot.mustache.TagIndicate;
import com.github.piotrkot.mustache.Tags;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * Partial tag type. Renders text at runtime based on file injection. Location
 * of the partial file must be provided in pair variables with name
 * of the partial as a key. Value can be as an InputStream, Path or
 * string file cnt.
 * @since 1.0
 */
@Slf4j
public final class Partial implements Tag {
    /**
     * Partial pattern.
     */
    private final transient Pattern patt;

    /**
     * Indicate.
     */
    private final TagIndicate indic;

    /**
     * Constructor.
     * @param indicate Indicate.
     */
    public Partial(final TagIndicate indicate) {
        this.indic = indicate;
        this.patt = Pattern.compile(
            String.format(
                "%s\\s*>\\s*([\\w\\.]+)\\s*%s",
                indicate.safeStart(),
                indicate.safeEnd()
            )
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
            final Object value = pairs.get(matcher.group(1));
            if (pairs.containsKey(matcher.group(1))) {
                try {
                    final Contents contents;
                    if (value instanceof Path) {
                        contents = new Contents((Path) value);
                    } else if (value instanceof InputStream) {
                        contents = new Contents((InputStream) value);
                    } else {
                        contents = new Contents(value.toString());
                    }
                    result.append(
                        new Tags(
                            new Section(this.indic),
                            new InvSection(this.indic),
                            new Variable(this.indic)
                        ).render(contents.asString(), pairs)
                    );
                } catch (final IOException ex) {
                    log.info("File {} not found", value);
                }
            }
            start = matcher.end();
        }
        result.append(tmpl.subSequence(start, tmpl.length()));
        return result.toString();
    }

}
