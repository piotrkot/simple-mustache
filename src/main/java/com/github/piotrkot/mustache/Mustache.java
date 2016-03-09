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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

/**
 * Mustache template.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public abstract class Mustache implements Template {
    /**
     * Template stream.
     */
    private final transient InputStream strm;
    /**
     * Path where partials can be found.
     */
    private final transient String pth;

    public Mustache(final String content, final Path directory) {
        this(new ByteArrayInputStream(content.getBytes()), directory);
    }

    public Mustache(final Path path) throws FileNotFoundException {
        this(new FileInputStream(path.toFile()), path.getParent());
    }

    public Mustache(final InputStream stream, final Path directory) {
        this.strm = stream;
        this.pth = directory.toString();
    }

    @Override
    public String supply(final Map<String, Object> pairs) {
        return null;
    }

    @Override
    public String path() {
        return this.pth;
    }

    @Override
    public abstract String start();

    @Override
    public abstract String end();
}
