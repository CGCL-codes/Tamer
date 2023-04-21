package org.ujac.util.exi;

import java.util.ArrayList;
import org.ujac.util.exi.ExpressionException;

/**
 * Name: BasicsTest<br>
 * Description: A test class for basic expression type operations.
 * 
 * @author lauerc
 */
public class BasicsTest extends BaseExpressionTest {

    /**
   * @see junit.framework.TestCase#setUp()
   */
    protected void setUp() throws Exception {
        super.setUp();
        ArrayList numbers = new ArrayList();
        numbers.add(new Integer(12));
        numbers.add(new Integer(43));
        numbers.add(new Integer(23));
        numbers.add(new Integer(14));
        numbers.add(new Integer(76));
        numbers.add(new Integer(64));
        numbers.add(new Integer(36));
        properties.put("numbers", numbers);
        properties.put("ten", new Integer(10));
        properties.put("eleven", new Integer(11));
        properties.put("negativeInt", new Integer(-13));
        properties.put("negativeFloat", new Float(-13.0F));
        properties.put("negativeDouble", new Double(-13.0));
        properties.put("yes", Boolean.TRUE);
        properties.put("no", Boolean.FALSE);
    }

    /**
   * Tests the isDefined operation for various types.
   * @throws ExpressionException In case an expression evaluation fails.
   */
    public void testIsDefined() throws ExpressionException {
        assertTrue(exi.evalBoolean("${ten isDefined}", properties, formatHelper));
        assertFalse(exi.evalBoolean("${nine isDefined}", properties, formatHelper));
    }

    /**
   * Tests the '=='/'eq' operation for various types.
   * @throws ExpressionException In case an expression evaluation fails.
   */
    public void testEqual() throws ExpressionException {
        assertFalse(exi.evalBoolean("${ten eq null}", properties, formatHelper));
        assertFalse(exi.evalBoolean("${ten == null}", properties, formatHelper));
        assertTrue(exi.evalBoolean("${nine == null}", properties, formatHelper));
        assertTrue(exi.evalBoolean("${nine eq null}", properties, formatHelper));
    }

    /**
   * Tests the '!='/'ne' operation for various types.
   * @throws ExpressionException In case an expression evaluation fails.
   */
    public void testNotEqual() throws ExpressionException {
        assertTrue(exi.evalBoolean("${ten != null}", properties, formatHelper));
        assertTrue(exi.evalBoolean("${ten ne null}", properties, formatHelper));
        assertFalse(exi.evalBoolean("${nine != null}", properties, formatHelper));
        assertFalse(exi.evalBoolean("${nine ne null}", properties, formatHelper));
    }

    /**
   * Tests the 'abs' operation for numeric types.
   * @throws ExpressionException In case an expression evaluation fails.
   */
    public void testAbs() throws ExpressionException {
        assertEquals(13, exi.evalInt("${negativeInt abs}", properties, formatHelper));
        assertEquals(13.0, exi.evalDouble("${negativeDouble abs}", properties, formatHelper), 0.0);
        assertEquals(13.0F, exi.evalFloat("${negativeFloat abs}", properties, formatHelper), 0.0F);
    }

    /**
   * Tests the 'toString' operation.
   * @throws ExpressionException In case an expression evaluation fails.
   */
    public void testToString() throws ExpressionException {
        assertEquals("10", exi.evalString("${ten toString}", properties, formatHelper));
    }

    /**
   * Tests the 'instanceof' operation.
   * @throws ExpressionException In case an expression evaluation fails.
   */
    public void testInstanceof() throws ExpressionException {
        assertTrue(exi.evalBoolean("${ten instanceof 'java.lang.Integer'}", properties, formatHelper));
        assertTrue(exi.evalBoolean("${ten instanceof 'java.lang.Number'}", properties, formatHelper));
        assertFalse(exi.evalBoolean("${ten instanceof 'java.lang.String'}", properties, formatHelper));
    }

    /**
   * Tests the 'format' operation for numeric types.
   * @throws ExpressionException In case an expression evaluation fails.
   */
    public void testFormat() throws ExpressionException {
        assertEquals("-013", exi.evalString("${negativeInt format '000'}", properties, formatHelper));
        assertEquals("-013.00", exi.evalString("${negativeDouble format '000.00'}", properties, formatHelper));
        assertEquals("-000,013.00", exi.evalString("${negativeFloat format '000,000.00'}", properties, formatHelper));
    }

    /**
   * Tests the logical and operation for numeric types.
   * @throws ExpressionException In case an expression evaluation fails.
   */
    public void testAnd() throws ExpressionException {
        assertTrue(exi.evalBoolean("${ten == 10 && yes}", properties, formatHelper));
        assertFalse(exi.evalBoolean("${ten == 10 and eleven != 11}", properties, formatHelper));
        assertFalse(exi.evalBoolean("${ten != 10 and yes}", properties, formatHelper));
        assertFalse(exi.evalBoolean("${${thirteen isDefined} and ${eleven isDefined}}", properties, formatHelper));
    }

    /**
   * Tests the logical and operation for numeric types.
   * @throws ExpressionException In case an expression evaluation fails.
   */
    public void testOr() throws ExpressionException {
        assertTrue(exi.evalBoolean("${ten == 10 || yes}", properties, formatHelper));
        assertFalse(exi.evalBoolean("${ten != 10 or eleven != 11}", properties, formatHelper));
        assertTrue(exi.evalBoolean("${ten != 10 or yes}", properties, formatHelper));
    }
}
