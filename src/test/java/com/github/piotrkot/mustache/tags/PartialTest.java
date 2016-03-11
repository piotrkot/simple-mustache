package com.github.piotrkot.mustache.tags;

import com.github.piotrkot.mustache.FileStream;
import com.github.piotrkot.mustache.TagIndicate;
import java.util.Collections;
import java.util.regex.Pattern;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test to Partial.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class PartialTest {
    /**
     * Should render partial.
     *
     * @throws Exception If fails.
     */
    @Test
    public void shouldRenderWithPartial() throws Exception {
        Assert.assertEquals(
            "body1\nhead1\nhead2\nbody2",
            new Partial(
                new TagIndicate() {
                    @Override
                    public String start() {
                        return Pattern.quote("[[");
                    }
                    @Override
                    public String end() {
                        return Pattern.quote("]]");
                    }
                },
                this.getClass().getResource("/").getPath()
            ).render(
                new FileStream(
                    this.getClass().getResourceAsStream("/prtl-test.mustache")
                ).asString(),
                Collections.emptyMap()
            )
        );
    }

    /**
     * Should render partial in partial.
     *
     * @throws Exception If fails.
     */
    @Test
    public void shouldRenderWithPartialInPartial() throws Exception {
        Assert.assertEquals(
            "body1\nhead1-2\nhead2-2\nhead1\nhead2\nbody2",
            new Partial(
                new TagIndicate() {
                    @Override
                    public String start() {
                        return Pattern.quote("[[");
                    }
                    @Override
                    public String end() {
                        return Pattern.quote("]]");
                    }
                },
                this.getClass().getResource("/").getPath()
            ).render(
                new FileStream(
                    this.getClass().getResourceAsStream("/prtl2-test.mustache")
                ).asString(),
                Collections.emptyMap()
            )
        );
    }

}
