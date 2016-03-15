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

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * File contents.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class Contents {
    /**
     * Template content.
     */
    private final transient String cont;
    /**
     * Constructor.
     * @param stream Input stream.
     * @throws IOException When fails.
     */
    public Contents(final InputStream stream) throws IOException {
        this(
            CharStreams
                .toString(new InputStreamReader(stream, Charsets.UTF_8))
        );
        Closeables.closeQuietly(stream);
    }
    /**
     * Constructor.
     * @param path File path.
     * @throws IOException When fails.
     */
    public Contents(final Path path) throws IOException {
        this(
            Files.lines(path, StandardCharsets.UTF_8)
                .collect(
                    Collectors.joining(
                        System.lineSeparator(),
                        "",
                        System.lineSeparator()
                    )
                )
        );
    }
    /**
     * Constructor.
     * @param content File contents.
     */
    public Contents(final String content) {
        this.cont = content;
    }

    /**
     * File contents.
     *
     * @return String contents.
     */
    public String asString() {
        return this.cont;
    }
}
