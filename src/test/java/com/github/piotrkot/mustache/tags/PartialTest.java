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
import com.github.piotrkot.mustache.TagIndicate;
import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.regex.Pattern;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test to Partial.
 * @since 1.0
 */
final class PartialTest {
    /**
     * Should render partial.
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderWithPartial() throws Exception {
        MatcherAssert.assertThat(
            new Partial(
                new SquareIndicate()
            ).render(
                new Contents(
                    "A [[>var]] B [[>miss]]"
                ).asString(),
                Collections.singletonMap("var", "inj")
            ),
            Matchers.is("A inj B ")
        );
    }

    /**
     * Should render partial stream.
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderWithPartialStream() throws Exception {
        MatcherAssert.assertThat(
            new Partial(
                new SquareIndicate()
            ).render(
                new Contents(
                    "A [[>str]] B"
                ).asString(),
                Collections.singletonMap(
                    "str",
                    new ByteArrayInputStream("aaa".getBytes())
                )
            ),
            Matchers.is("A aaa B")
        );
    }

    /**
     * Should render partial path.
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderWithPartialPath() throws Exception {
        MatcherAssert.assertThat(
            new Partial(
                new SquareIndicate()
            ).render(
                new Contents(
                    "A [[>pth]] B [[>inv]]"
                ).asString(),
                new MapOf<CharSequence, Object>(
                    new MapEntry<>(
                        "pth",
                        Paths.get(
                            this.getClass().getResource("/prtl.mustache").toURI()
                        )
                    ),
                    new MapEntry<>("inv", Paths.get("willNotFind"))
                )
            ),
            Matchers.is("A head1\nhead2\n B ")
        );
    }

    /**
     * Should render partial with sequence.
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderWithPartialSequence() throws Exception {
        MatcherAssert.assertThat(
            new Partial(
                new SquareIndicate()
            ).render(
                new Contents(
                    "A [[>seq]] B"
                ).asString(),
                new MapOf<>(
                    new MapEntry<>(
                        "seq", "[[#a]][[x]][[/a]] [[^b]]D[[/b]] [[>more]] [[y]]"
                    ),
                    new MapEntry<>(
                        "a",
                        Collections.singletonList(new MapOf<CharSequence, Object>("x", "X"))
                    ),
                    new MapEntry<>("b", Boolean.FALSE),
                    new MapEntry<>("y", "Y")
                )
            ),
            Matchers.is("A X D [[>more]] Y B")
        );
    }

    /**
     * Should render valid tags.
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderValidTags() throws Exception {
        MatcherAssert.assertThat(
            new Partial(
                new SquareIndicate()
            ).render(
                "[[ >  aA0._ ]] ",
                new MapOf<>("aA0._", "O")
            ),
            Matchers.is("O ")
        );
    }

    /**
     * Tag indicate with double square parentheses.
     * @since 1.0
     */
    private class SquareIndicate implements TagIndicate {
        @Override
        public String safeStart() {
            return Pattern.quote("[[");
        }

        @Override
        public String safeEnd() {
            return Pattern.quote("]]");
        }
    }
}
