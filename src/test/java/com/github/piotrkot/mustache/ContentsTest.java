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

import java.nio.file.Paths;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for Contents class.
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class ContentsTest {
    /**
     * Expected content.
     */
    private static final String CONTENT = "head1\nhead2\n";
    /**
     * Resource to read.
     */
    private static final String RESOURCE = "/prtl.mustache";
    /**
     * Should read stream. For safety check if can read twice.
     * @throws Exception If fails.
     */
    @Test
    public void shouldReadStream() throws Exception {
        MatcherAssert.assertThat(
            new Contents(
                this.getClass().getResourceAsStream(ContentsTest.RESOURCE)
            ).asString(),
            Matchers.is(ContentsTest.CONTENT)
        );
        MatcherAssert.assertThat(
            new Contents(
                this.getClass().getResourceAsStream(ContentsTest.RESOURCE)
            ).asString(),
            Matchers.is(ContentsTest.CONTENT)
        );
    }
    /**
     * Should read string.
     * @throws Exception If fails.
     */
    @Test
    public void shouldReadString() throws Exception {
        MatcherAssert.assertThat(
            new Contents(ContentsTest.CONTENT).asString(),
            Matchers.is(ContentsTest.CONTENT)
        );
    }

    /**
     * Should read path.
     * @throws Exception If fails.
     */
    @Test
    public void shouldReadPath() throws Exception {
        MatcherAssert.assertThat(
            new Contents(
                Paths.get(
                    this.getClass().getResource(ContentsTest.RESOURCE).toURI()
                )
            ).asString(),
            Matchers.is(ContentsTest.CONTENT)
        );
    }
}
