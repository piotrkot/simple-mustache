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
package com.github.piotrkot.mustache;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.Collections;
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Tests for Mustache class.
 *
 * @since 1.0
 */
final class MustacheTest {

    /**
     * Should supply with pairs.
     *
     * @throws Exception When fails.
     */
    @Test
    void shouldSupply() throws Exception {
        MatcherAssert.assertThat(
            new Mustache("1 {{a}}").supply(new MapOf<>("a", "A")),
            Matchers.is("1 A")
        );
    }

    /**
     * Should supply from Stream.
     *
     * @throws Exception When fails.
     */
    @Test
    void shouldSupplyFromStream() throws Exception {
        MatcherAssert.assertThat(
            new Mustache(new ByteArrayInputStream("1 {{b}}".getBytes()))
                .supply(new MapOf<>("b", "B")),
            Matchers.is("1 B")
        );
    }

    /**
     * Should supply from path.
     *
     * @throws Exception When fails.
     */
    @Test
    void shouldSupplyFromPath() throws Exception {
        MatcherAssert.assertThat(
            new Mustache(
                Paths.get(
                    this.getClass().getResource("/prtl.mustache").toURI()
                )
            ).supply(Collections.emptyMap()),
            Matchers.is("head1\nhead2\n")
        );
    }

    /**
     * Should supply partial in section.
     *
     * @throws Exception When fails.
     */
    @Test
    void shouldSupplyPartialInSection() throws Exception {
        MatcherAssert.assertThat(
            new Mustache(
                new ByteArrayInputStream("{{#x}}{{>y}}{{/x}}".getBytes())
            ).supply(
                new MapOf<CharSequence, Object>(
                    new MapEntry<>("y", "inj"),
                    new MapEntry<>("x", new ListOf<>("one", "two"))
                )
            ),
            Matchers.is("injinj")
        );
    }
}
