package com.github.piotrkot.mustache.tags;

import com.github.piotrkot.mustache.TagIndicate;
import com.google.common.collect.ImmutableMap;
import java.util.regex.Pattern;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for Variable.
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class VariableTest {

    /**
     * Should render variable.
     *
     * @throws Exception If fails.
     */
    @Test
    public void shouldRenderVariable() throws Exception {
        Assert.assertEquals(
            "1 2 three [[four]]  [[>6]] [[#7]] [[/8]] [[^9]]",
            new Variable(
                new TagIndicate() {
                    @Override
                    public String safeStart() {
                        return Pattern.quote("[[");
                    }
                    @Override
                    public String safeEnd() {
                        return Pattern.quote("]]");
                    }
                }
            ).render(
                "1 [[2]] [[3]] [[4]] [[5]] [[>6]] [[#7]] [[/8]] [[^9]]",
                ImmutableMap.of("2", 2, "3", "three", "4", "[[four]]")
            )
        );
    }

}
