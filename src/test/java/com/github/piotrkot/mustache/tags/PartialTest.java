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

import com.github.piotrkot.mustache.FileStream;
import com.github.piotrkot.mustache.TagIndicate;
import java.util.Collections;
import java.util.regex.Pattern;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test to Partial.
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class PartialTest {
    /**
     * Path to partials.
     */
    private static final String PATH = PartialTest.class
        .getResource("/").getPath();

    /**
     * Should render partial.
     * @throws Exception If fails.
     */
    @Test
    public void shouldRenderWithPartial() throws Exception {
        MatcherAssert.assertThat(
            new Partial(
                new SquareIndicate()
            ).render(
                new FileStream(
                    this.getClass().getResourceAsStream("/prtl-test.mustache")
                ).asString(),
                Collections.emptyMap()
            ),
            Matchers.is("body1\nhead1\nhead2\nbody2")
        );
    }

    /**
     * Should render partial in partial.
     * @throws Exception If fails.
     */
    @Test
    @Ignore
    public void shouldRenderWithPartialInPartial() throws Exception {
        MatcherAssert.assertThat(
            new Partial(
                new SquareIndicate()
            ).render(
                new FileStream(
                    this.getClass().getResourceAsStream("/prtl2-test.mustache")
                ).asString(),
                Collections.emptyMap()
            ),
            Matchers.is("body1\nhead1-2\nhead2-2\nhead1\nhead2\nbody2")
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
