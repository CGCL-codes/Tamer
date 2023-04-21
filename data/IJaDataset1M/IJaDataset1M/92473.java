package org.databene.feed4junit.benerator.coverage;

import java.util.ArrayList;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.databene.benerator.anno.Coverage;
import org.databene.benerator.anno.Granularity;
import org.databene.feed4junit.Feed4JUnitTestCase;
import org.databene.feed4junit.Feeder;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the coverage parameter generation for single numbers.<br/><br/>
 * Created: 23.07.2011 18:26:57
 * @since 1.0
 * @author Volker Bergmann
 */
@RunWith(Feeder.class)
@Coverage
public class SingleNumberCoverageTest extends Feed4JUnitTestCase {

    static ArrayList<Integer> minMaxSet = new ArrayList<Integer>();

    @Test
    public void testMinMax(@Min(-3) @Max(11) @Granularity(2) int n) {
        minMaxSet.add(n);
    }

    @AfterClass
    public static void verifyMinMax() {
        assertObjectSequence(minMaxSet, -3, -1, 1, 9, 11, 3, 5, 7);
    }
}
