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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Mustache template.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public abstract class AbstractMustache implements Template {
    /**
     * Template content.
     */
    private final transient String str;
    /**
     * Mustache tags.
     */
    private final Collection<Tag> tags;
    /**
     * Constructor.
     *
     * @param stream Template stream.
     * @throws IOException When fails.
     */
    public AbstractMustache(final InputStream stream) throws IOException {
        this(new Contents(stream).asString());
    }
    /**
     * Constructor.
     *
     * @param path Template path.
     * @throws IOException When fails.
     */
    public AbstractMustache(final Path path) throws IOException {
        this(new Contents(path).asString());
    }
    /**
     * Constructor.
     *
     * @param content Template content.
     */
    public AbstractMustache(final String content) {
        this.str = content;
        this.tags = Arrays.asList(
            new Partial(this),
            new Section(this),
            new InvSection(this),
            new Variable(this)
        );
    }

    @Override
    public final String supply(final Map<String, Object> pairs) {
        String rendered = this.str;
        for (final Tag tag : this.tags) {
            rendered = tag.render(rendered, pairs);
        }
        return rendered;
    }

    @Override
    public final String safeStart() {
        return Pattern.quote(this.start());
    }

    @Override
    public final String safeEnd() {
        return Pattern.quote(this.end());
    }

    /**
     * String that starts the tag.
     * @return Code string.
     */
    public abstract String start();

    /**
     * String that ends the tag.
     * @return Code string.
     */
    public abstract String end();
}
