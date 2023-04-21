package com.volantis.styling.unit.engine.matchers.composites;

import com.volantis.styling.impl.engine.matchers.composites.CompositeState;
import com.volantis.styling.impl.engine.matchers.composites.DescendantState;

/**
 * Test the {@link DescendantState}.
 */
public class DescendantStateTestCase extends CompositeStateTestCaseAbstract {

    protected CompositeState createState() {
        return new DescendantState();
    }

    /**
     * Test that state remembers that the context has been matched within
     * descendant nodes.
     *
     * <p>e.g. if the element marked with a <code>#</code> matches the
     * contextual selector then all the elements marked with a <code>*</code>
     * expect to know that the context matched.</p>
     *
     * <pre>
     *     a#
     *     +- b*
     *        +- c*
     *           +- d*
     * </pre>
     */
    public void testDescendantsKnowContextMatched() {
        state.beforeStartElement();
        state.contextMatched();
        for (int i = 0; i < 10; i += 1) {
            state.beforeStartElement();
            assertTrue("Descendants should know context matched", state.hasDirectRelationship());
        }
    }

    /**
     * Test that the context matched state is cleared outside the element.
     *
     * <p>e.g. if the element marked with a <code>#</code> matches the
     * contextual selector then all the elements marked with a <code>*</code>
     * do not expect to know that the context matched.</p>
     *
     * <pre>
     *     a*
     *     +- b#
     *     c*
     * </pre>
     */
    public void testContextMatchedClearedOutsideElement() {
        state.beforeStartElement();
        state.beforeStartElement();
        state.contextMatched();
        state.afterEndElement();
        assertFalse("Context matched state should be cleared outside the" + " element", state.hasDirectRelationship());
    }
}
