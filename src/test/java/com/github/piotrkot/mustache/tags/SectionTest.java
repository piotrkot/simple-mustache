package com.github.piotrkot.mustache.tags;

import com.github.piotrkot.mustache.TagIndicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.regex.Pattern;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for Section.
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class SectionTest {
    /**
     * Should render section.
     * @throws Exception If fails.
     */
    @Test
    public void shouldRenderSection() throws Exception {
        Assert.assertEquals(
            "1 X YY",
            new Section(
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
                "1 [[#2]]X[[/2]] [[#3]]Y[[/3]]",
                ImmutableMap.of("2", true, "3", ImmutableList.of("a", "b"))
            )
        );
    }

    /**
     * Should render section with variable.
     * @throws Exception If fails.
     */
    @Test
    public void shouldRenderSectionWithVariable() throws Exception {
        Assert.assertEquals(
            "1 X iks y1 y2 ",
            new Section(
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
                "1 [[#2]]X [[x]][[/2]] [[#3]][[y]] [[/3]]",
                ImmutableMap.of(
                    "2", ImmutableMap.of("x", "iks"),
                    "3", ImmutableList.of(
                        ImmutableMap.of("y", "y1"),
                        ImmutableMap.of("y", "y1")
                    )
                )
            )
        );
    }

}
