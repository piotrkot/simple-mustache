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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.Collections;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for Mustache class.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class MustacheTest {

    /**
     * Should supply with pairs.
     *
     * @throws Exception When fails.
     */
    @Test
    public void shouldSupply() throws Exception {
        MatcherAssert.assertThat(
            new Mustache("1 {{a}}").supply(ImmutableMap.of("a", "A")),
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
            new Mustache(new ByteArrayInputStream("1 {{b}}".getBytes()))
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
    public void shouldSupplyPartialInSection() throws Exception {
        MatcherAssert.assertThat(
            new Mustache(
                new ByteArrayInputStream("{{#x}}{{>y}}{{/x}}".getBytes())
            ).supply(
                ImmutableMap.of(
                    "y", "inj",
                    "x", ImmutableList.of("one", "two")
                )
            ),
            Matchers.is("injinj\n")
        );
    }
}
