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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * File contents representation.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class File {
    /**
     * Template content.
     */
    private final transient String cont;
    /**
     * Constructor.
     * @param stream Input stream.
     */
    public File(final InputStream stream) throws IOException {
        this(File.asString(stream));
    }
    /**
     * Constructor.
     * @param path File path.
     */
    public File(final Path path) throws IOException {
        this(
            Files.lines(path, StandardCharsets.UTF_8).reduce(
                "",
                (pre, post) -> String
                    .join(System.lineSeparator(), pre, post)
            )
        );
    }
    /**
     * Constructor.
     * @param content File contents.
     */
    public File(final String content) {
        this.cont = content;
    }

    /**
     * File contents.
     *
     * @return String contents.
     */
    public String content() {
        return this.cont;
    }

    /**
     * String representation of a stream.
     *
     * @return Content as string.
     * @throws IOException When fails.
     */
    private static String asString(final InputStream stream) throws IOException {
        final BufferedReader buff = new BufferedReader(
            new InputStreamReader(stream, StandardCharsets.UTF_8)
        );
        final StringBuilder bld = new StringBuilder(1024);
        String str = buff.readLine();
        while (str != null) {
            bld.append(str);
            str = buff.readLine();
        }
        return bld.toString();
    }

}
