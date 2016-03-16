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

import com.google.common.collect.ImmutableMap;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for Abstract mustache class.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class AbstrMustacheTest {

    /**
     * Should supply with pairs.
     *
     * @throws Exception When fails.
     */
    @Test
    public void shouldSupply() throws Exception {
        MatcherAssert.assertThat(
            new FakeMustache("1 {{a}}").supply(ImmutableMap.of("a", "A")),
            Matchers.is("1 A")
        );
    }

    /**
     * Should supply from Stream.
     *
     * @throws Exception When fails.
     */
    @Test
    public void shouldSupplyFromStream() throws Exception {
        MatcherAssert.assertThat(
            new FakeMustache(new ByteArrayInputStream("1 {{b}}".getBytes()))
                .supply(ImmutableMap.of("b", "B")),
            Matchers.is("1 B\n")
        );
    }

    /**
     * Should supply from path.
     *
     * @throws Exception When fails.
     */
    @Test
    public void shouldSupplyFromPath() throws Exception {
        MatcherAssert.assertThat(
            new FakeMustache(
                Paths.get(
                    this.getClass().getResource("/prtl.mustache").toURI()
                )
            ).supply(Collections.emptyMap()),
            Matchers.is("head1\nhead2\n")
        );
    }

    /**
     * Simple mustache.
     */
    class FakeMustache extends AbstractMustache {
        /**
         * Constructor.
         *
         * @param content Content.
         */
        FakeMustache(final String content) {
            super(content);
        }
        /**
         * Constructor.
         *
         * @param stream Stream.
         * @throws IOException When fails.
         */
        FakeMustache(final InputStream stream) throws IOException {
            super(stream);
        }
        /**
         * Constructor.
         *
         * @param path Path.
         * @throws IOException When fails.
         */
        FakeMustache(final Path path) throws IOException {
            super(path);
        }
        @Override
        public String start() {
            return "{{";
        }
        @Override
        public String end() {
            return "}}";
        }
    }
}
