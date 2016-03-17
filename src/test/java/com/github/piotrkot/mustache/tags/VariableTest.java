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
import com.google.common.collect.ImmutableMap;
import java.util.regex.Pattern;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for Variable.
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class VariableTest {

    /**
     * Should render variable.
     * @throws Exception If fails.
     */
    @Test
    public void shouldRenderVariable() throws Exception {
        MatcherAssert.assertThat(
            new Variable(
                new SquareIndicate()
            ).render(
                "1 [[2]] [[3]][[4]] [[5]] [[>6]] [[#7]] [[/7]]",
                ImmutableMap.of("2", 2, "3", "three", "4", "[[four]]")
            ),
            Matchers.is("1 2 three[[four]]  [[>6]] [[#7]] [[/7]]")
        );
    }

    /**
     * Should not render variable inside section.
     * @throws Exception If fails.
     * @checkstyle MultipleStringLiterals (8 lines)
     */
    @Test
    public void shouldNotRenderVariableInside() throws Exception {
        MatcherAssert.assertThat(
            new Variable(
                new SquareIndicate()
            ).render(
                "[[#2]][[1]][[/2]][[^4]][[5]][[/4]] [[#7]][[6]][[>9]][[/7]]",
                ImmutableMap.of("1", "A", "5", "B", "6", "C")
            ),
            Matchers.is(
                "[[#2]][[1]][[/2]][[^4]][[5]][[/4]] [[#7]][[6]][[>9]][[/7]]"
            )
        );
    }

    /**
     * Should not render variable inside nested section.
     * @throws Exception If fails.
     * @checkstyle MultipleStringLiterals (8 lines)
     */
    @Test
    public void shouldNotRenderVariableInsideNested() throws Exception {
        MatcherAssert.assertThat(
            new Variable(
                new SquareIndicate()
            ).render(
                "[[#2]][[11]] [[^4]][[15]][[/4]] [[/2]]",
                ImmutableMap.of("11", "AA", "15", "BB")
            ),
            Matchers.is("[[#2]][[11]] [[^4]][[15]][[/4]] [[/2]]")
        );
    }

    /**
     * Should render valid tags.
     * @throws Exception If fails.
     */
    @Test
    public void shouldRenderValidTags() throws Exception {
        MatcherAssert.assertThat(
            new Variable(
                new SquareIndicate()
            ).render(
                "[[aA0._ ]] ",
                ImmutableMap.of("aA0._", "XX")
            ),
            Matchers.is("XX ")
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
