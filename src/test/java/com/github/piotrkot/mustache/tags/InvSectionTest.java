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

import com.github.piotrkot.mustache.TagIndicate;
import java.util.Collections;
import java.util.regex.Pattern;
import org.cactoos.iterable.IterableOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Tests for Inverted section.
 *
 * @since 1.0
 */
final class InvSectionTest {
    /**
     * Should render inverted section.
     *
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderInvSection() throws Exception {
        MatcherAssert.assertThat(
            new InvSection(
                new SquareIndicate()
            ).render(
                "1 [[^2]]X[[/2]] [[^3]]Y[[/3]]",
                new MapOf<CharSequence, Object>(
                    new MapEntry<>("2", false),
                    new MapEntry<>("3", Collections.emptyList())
                )
            ),
            Matchers.is("1 X Y")
        );
    }

    /**
     * Should render inverted section with variable.
     *
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderInvSectionWithVariable() throws Exception {
        MatcherAssert.assertThat(
            new InvSection(new SquareIndicate()).render(
                "1 [[^a]]X [[x]][[/a]] [[y]][[^c]]W[[/c]]",
                new MapOf<>(
                    new MapEntry<>("a", Collections.emptyList()),
                    new MapEntry<>("c", false),
                    new MapEntry<>("x", "iks")
                )
            ),
            Matchers.is("1 X iks [[y]]W")
        );
    }

    /**
     * Should not render inverted section.
     *
     * @throws Exception If fails.
     */
    @Test
    void shouldNotRenderInvSection() throws Exception {
        MatcherAssert.assertThat(
            new InvSection(new SquareIndicate()).render(
                "[[^A]]A[[/A]][[^B]]B[[/B]][[^C]]C[[/C]][[^D]]D[[/D]]",
                new MapOf<>(
                    new MapEntry<>("A", Collections.singleton("nonempty")),
                    new MapEntry<>("B", true),
                    new MapEntry<>("D", 1)
                )
            ),
            Matchers.is("")
        );
    }

    /**
     * Should render inverted section with subsection.
     *
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderInvSectionWithSubsection() throws Exception {
        MatcherAssert.assertThat(
            new InvSection(new SquareIndicate()).render(
                "1 [[^o]]-X [[q]][[^i]]Y [[w]][[/i]][[/o]]",
                new MapOf<>(
                    new MapEntry<>("q", "Q"),
                    new MapEntry<>("o", Collections.emptyList()),
                    new MapEntry<>("i", Collections.emptyList()),
                    new MapEntry<>("w", "W")
                )
            ),
            Matchers.is("1 -X QY W")
        );
    }

    /**
     * Should render valid tags.
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderValidTags() throws Exception {
        MatcherAssert.assertThat(
            new InvSection(
                new SquareIndicate()
            ).render(
                "[[ ^aA0._  ]] [[ / aA0._ ]]",
                new MapOf<>("aA0._", false)
            ),
            Matchers.is(" ")
        );
    }

    /**
     * Should render on new lines.
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderOnNewlines() throws Exception {
        MatcherAssert.assertThat(
            new InvSection(
                new SquareIndicate()
            ).render(
                "[[^nl]]\n[[line]]\n[[/nl]]",
                new MapOf<CharSequence, Object>(
                    new MapEntry<>("nl", Collections.emptyList()),
                    new MapEntry<>("line", "1-line")
                )
            ),
            Matchers.is("\n1-line\n")
        );
    }

    /**
     * Should render iterable.
     * @throws Exception If fails.
     * @checkstyle MultipleStringLiterals (12 lines)
     */
    @Test
    void shouldRenderIterable() throws Exception {
        MatcherAssert.assertThat(
            new InvSection(
                new SquareIndicate()
            ).render(
                "1 [[^IT]]X[[/IT]] 3",
                new MapOf<>("IT", new IterableOf<>())
            ),
            Matchers.is("1 X 3")
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
