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
                "1 [[#o]]-X [[q]][[#i]]Y [[w]][[/i]][[/o]]",
                ImmutableMap.of(
                    "o", ImmutableList.of(
                        ImmutableMap.of("q", "Q1"),
                        ImmutableMap.of("q", "Q2")
                    ),
                    "i", ImmutableList.of(
                        ImmutableMap.of("w", "W1"),
                        ImmutableMap.of("w", "W2")
                    )
                )
            ),
            Matchers.is("1 -X Q1Y W1Y W2-X Q2Y W1Y W2")
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
