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
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Tests for Section.
 * @since 1.0
 */
final class SectionTest {
    /**
     * Should render section.
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderSection() throws Exception {
        MatcherAssert.assertThat(
            new Section(
                new SquareIndicate()
            ).render(
                "1 [[#2]]X[[/2]] [[#3]]Y[[/3]]",
                new MapOf<CharSequence, Object>(
                    new MapEntry<>("2", true),
                    new MapEntry<>("3", new ListOf<>("12", "32"))
                )
            ),
            Matchers.is("1 X YY")
        );
    }

    /**
     * Should not render section.
     * @throws Exception If fails.
     */
    @Test
    void shouldNotRenderSection() throws Exception {
        MatcherAssert.assertThat(
            new Section(
                new SquareIndicate()
            ).render(
                "[[#A]]X[[/A]][[#B]]Y[[/B]][[#C]]Z[[/C]][[#D]]W[[/D]]",
                new MapOf<>(
                    new MapEntry<>("A", false),
                    new MapEntry<>("B", Collections.emptyList()),
                    new MapEntry<>("C", 1)
                )
            ),
            Matchers.is("")
        );
    }

    /**
     * Should render section with variable.
     * @throws Exception If fails.
     * @checkstyle MultipleStringLiterals (10 lines)
     */
    @Test
    void shouldRenderSectionWithVariable() throws Exception {
        MatcherAssert.assertThat(
            new Section(new SquareIndicate()).render(
                "1 [[#a]]X [[x]][[/a]] [[#b]][[y]] [[/b]]",
                new MapOf<CharSequence, Object>(
                    new MapEntry<>(
                        "a",
                        Collections.singletonList(new MapOf<CharSequence, Object>("x", "iks"))
                    ),
                    new MapEntry<>(
                        "b",
                        new ListOf<>(
                            new MapOf<CharSequence, Object>("y", "y1"),
                            new MapOf<CharSequence, Object>("y", "y2")
                        )
                    )
                )
            ),
            Matchers.is("1 X iks y1 y2 ")
        );
    }

    /**
     * Should render section with subsection.
     * @throws Exception If fails.
     * @checkstyle MultipleStringLiterals (13 lines)
     */
    @Test
    void shouldRenderSectionWithSubsection() throws Exception {
        MatcherAssert.assertThat(
            new Section(new SquareIndicate()).render(
                "1 [[#o]]-X [[q]]+[[#i]]Y [[w]][[/i]] [[/o]]",
                new MapOf<>(
                    new MapEntry<>(
                        "o",
                        new ListOf<>(
                            new MapOf<CharSequence, Object>(
                                new MapEntry<>("q", "Q1"),
                                new MapEntry<>(
                                    "i",
                                    new ListOf<>(
                                        new MapOf<CharSequence, Object>("w", "W1"),
                                        new MapOf<CharSequence, Object>("w", "W2")
                                    )
                                )
                            ),
                            new MapOf<CharSequence, Object>(
                                new MapEntry<>("q", "Q2"),
                                new MapEntry<>(
                                    "i",
                                    new ListOf<>(
                                        new MapOf<CharSequence, Object>("w", "W3"),
                                        new MapOf<CharSequence, Object>("w", "W4")
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            Matchers.is("1 -X Q1+Y W1Y W2 -X Q2+Y W3Y W4 ")
        );
    }

    /**
     * Should render section nested.
     * @throws Exception If fails.
     * @checkstyle MultipleStringLiterals (13 lines)
     */
    @Test
    void shouldRenderSectionNested() throws Exception {
        MatcherAssert.assertThat(
            new Section(new SquareIndicate()).render(
                "[[#out]][[v]][[#in]][[vv]][[/in]][[^in]]none[[/in]][[/out]]",
                new MapOf<>(
                    new MapEntry<>(
                        "out",
                        new ListOf<>(
                            new MapOf<CharSequence, Object>(
                                new MapEntry<>("v", "V1"),
                                new MapEntry<>(
                                    "in",
                                    new ListOf<>(
                                        new MapOf<CharSequence, Object>("vv", "X1"),
                                        new MapOf<CharSequence, Object>("vv", "X2")
                                    )
                                )
                            ),
                            new MapOf<CharSequence, Object>(
                                new MapEntry<>("v", "V2"),
                                new MapEntry<>("in", Collections.emptyList())
                            )
                        )
                    )
                )
            ),
            Matchers.is("V1X1X2V2none")
        );
    }

    /**
     * Should render valid tags.
     * @throws Exception If fails.
     */
    @Test
    void shouldRenderValidTags() throws Exception {
        MatcherAssert.assertThat(
            new Section(
                new SquareIndicate()
            ).render(
                "[[ #aA0._ ]] [[ / aA0._ ]]",
                new MapOf<>("aA0._", true)
            ),
            Matchers.is(" ")
        );
    }

    /**
     * Should render on new lines.
     * @throws Exception If fails.
     * @checkstyle MultipleStringLiterals (12 lines)
     */
    @Test
    void shouldRenderOnNewlines() throws Exception {
        MatcherAssert.assertThat(
            new Section(
                new SquareIndicate()
            ).render(
                "[[#nl]]\n[[line]]\n[[/nl]]",
                new MapOf<>(
                    new MapEntry<>(
                        "nl",
                        new ListOf<>(
                            new MapOf<CharSequence, Object>("line", "1-line"),
                            new MapOf<CharSequence, Object>("line", "2-line")
                        )
                    )
                )
            ),
            Matchers.is("\n1-line\n\n2-line\n")
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
            new Section(
                new SquareIndicate()
            ).render(
                "1 [[#IT]]X [[/IT]]3",
                new MapOf<>("IT", new IterableOf<>("aaa", "bbb"))
            ),
            Matchers.is("1 X X 3")
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
