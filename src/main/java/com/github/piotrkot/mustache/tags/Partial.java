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

import com.github.piotrkot.mustache.FileStream;
import com.github.piotrkot.mustache.Tag;
import com.github.piotrkot.mustache.TagIndicate;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
@Slf4j
public final class Partial implements Tag {
    /**
     * Partial pattern.
     */
    private final transient Pattern patt;

    /**
     * Constructor.
     *
     * @param indicate Indicate.
     */
    public Partial(final TagIndicate indicate) {
        this.patt = Pattern.compile(
            String.format(
                "%s>([^\\s]+)%s",
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
            final Object value = pairs.get(name);
            if (pairs.containsKey(name)) {
                try {
                    File file;
                    if (value instanceof Path) {
                        file = new File((Path) value);
                    } else if (value instanceof InputStream) {
                        file = new File((InputStream) value);
                    } else {
                        file = new File(value.toString());
                    }
                    result.append(file.content());
                    result.append(System.lineSeparator());
                } catch (final IOException ex) {
                    log.info("File {} not found", value);
                }
            }
            start = matcher.end();
        }
        result.append(tmpl.substring(start, tmpl.length()));
        return result.toString();
    }

    class File {
        private final String cnt;
        public File(final InputStream stream) throws IOException {
            this(new FileStream(stream).asString());
        }
        public File(final Path path) throws IOException {
            this(
                Files.lines(path, StandardCharsets.UTF_8).reduce(
                    "",
                    (pre, post) -> String
                        .join(System.lineSeparator(), pre, post)
                )
            );
        }
        public File(final String content) {
            this.cnt = content;
        }

        public String content() {
            return this.cnt;
        }
    }
}
