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
package com.github.piotrkot.mustache.tags;

import com.github.piotrkot.mustache.TagIndicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.regex.Pattern;
import org.cactoos.iterable.IterableOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for Section.
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class SectionTest {
    /**
     * Should render section.
     * @throws Exception If fails.
     */
    @Test
    public void shouldRenderSection() throws Exception {
        MatcherAssert.assertThat(
            new Section(
                new SquareIndicate()
            ).render(
                "1 [[#2]]X[[/2]] [[#3]]Y[[/3]]",
                ImmutableMap.of("2", true, "3", ImmutableList.of("12", "32"))
            ),
            Matchers.is("1 X YY")
        );
    }
    /**
     * Should not render section.
     * @throws Exception If fails.
     */
    @Test
    public void shouldNotRenderSection() throws Exception {
        MatcherAssert.assertThat(
            new Section(
                new SquareIndicate()
            ).render(
                "[[#A]]X[[/A]][[#B]]Y[[/B]][[#C]]Z[[/C]][[#D]]W[[/D]]",
                ImmutableMap.of(
                    "A", false,
                    "B", Collections.emptyList(),
                    "C", 1
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
    public void shouldRenderSectionWithVariable() throws Exception {
        MatcherAssert.assertThat(
            new Section(new SquareIndicate()).render(
                "1 [[#a]]X [[x]][[/a]] [[#b]][[y]] [[/b]]",
                ImmutableMap.of(
                    "a", Collections.singletonList(ImmutableMap.of("x", "iks")),
                    "b", ImmutableList.of(
                        ImmutableMap.of("y", "y1"),
                        ImmutableMap.of("y", "y2")
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
    public void shouldRenderSectionWithSubsection() throws Exception {
        MatcherAssert.assertThat(
            new Section(new SquareIndicate()).render(
                "1 [[#o]]-X [[q]]+[[#i]]Y [[w]][[/i]] [[/o]]",
                ImmutableMap.of(
                    "o", ImmutableList.of(
                        ImmutableMap.of(
                            "q", "Q1",
                            "i", ImmutableList.of(
                                ImmutableMap.of("w", "W1"),
                                ImmutableMap.of("w", "W2")
                            )
                        ),
                        ImmutableMap.of(
                            "q", "Q2",
                            "i", ImmutableList.of(
                                ImmutableMap.of("w", "W3"),
                                ImmutableMap.of("w", "W4")
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
    public void shouldRenderSectionNested() throws Exception {
        MatcherAssert.assertThat(
            new Section(new SquareIndicate()).render(
                "[[#out]][[v]][[#in]][[vv]][[/in]][[^in]]none[[/in]][[/out]]",
                ImmutableMap.of(
                    "out", ImmutableList.of(
                        ImmutableMap.of(
                            "v", "V1",
                            "in", ImmutableList.of(
                                ImmutableMap.of("vv", "X1"),
                                ImmutableMap.of("vv", "X2")
                            )
                        ),
                        ImmutableMap.of(
                            "v", "V2",
                            "in", Collections.emptyList()
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
    public void shouldRenderValidTags() throws Exception {
        MatcherAssert.assertThat(
            new Section(
                new SquareIndicate()
            ).render(
                "[[ #aA0._ ]] [[ / aA0._ ]]",
                ImmutableMap.of("aA0._", true)
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
    public void shouldRenderOnNewlines() throws Exception {
        MatcherAssert.assertThat(
            new Section(
                new SquareIndicate()
            ).render(
                "[[#nl]]\n[[line]]\n[[/nl]]",
                ImmutableMap.of(
                    "nl",
                    ImmutableList.of(
                        ImmutableMap.of("line", "1-line"),
                        ImmutableMap.of("line", "2-line")
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
    public void shouldRenderIterable() throws Exception {
        MatcherAssert.assertThat(
            new Section(
                new SquareIndicate()
            ).render(
                "1 [[#IT]]X [[/IT]]3",
                ImmutableMap.of("IT", new IterableOf<>("aaa", "bbb"))
            ),
            Matchers.is("1 X X 3")
        );
    }

    /**
     * Tag indicate with double square parentheses.
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
