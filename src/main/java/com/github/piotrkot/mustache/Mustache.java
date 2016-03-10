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

import com.github.piotrkot.mustache.tags.InvSection;
import com.github.piotrkot.mustache.tags.Partial;
import com.github.piotrkot.mustache.tags.Section;
import com.github.piotrkot.mustache.tags.Variable;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Mustache template.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public abstract class Mustache implements Template {
    /**
     * Template content.
     */
    private final transient String str;
    /**
     * Path where partials can be found.
     */
    private final transient String pth;

    private final Collection<Tag> tags = Arrays.asList(
        new Partial(this),
        new Section(),
        new InvSection(),
        new Variable()
    );

    public Mustache(final InputStream stream) throws IOException {
        this(stream, Paths.get("."));
    }

    public Mustache(final InputStream stream, final Path directory) throws IOException {
        this(Mustache.stringed(stream), directory);
    }

    public Mustache(final Path path) throws IOException {
        this(path, path.getParent());
    }

    public Mustache(final Path path, final Path directory) throws IOException {
        this(
            Files.lines(path, StandardCharsets.UTF_8)
                .reduce("", String::concat),
            directory
        );
    }

    public Mustache(final String content) {
        this(content, Paths.get("."));
    }

    public Mustache(final String content, final Path directory) {
        this.str = content;
        this.pth = directory.toString();
    }

    @Override
    public String supply(final Map<String, Object> pairs) {
        for (final Tag tag : this.tags) {
            tag.render(this.str, pairs);
        }
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

    private static String stringed(final InputStream stream) throws IOException {
        final BufferedReader buff = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8)
        );
        String str = null;
        final StringBuilder bld = new StringBuilder(1024);
        while ((str = buff.readLine()) != null) {
            bld.append(str);
        }
        return bld.toString();
    }
}
