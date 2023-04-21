package org.npsnet.v.math;

import org.npsnet.v.kernel.Module;
import org.npsnet.v.services.math.BooleanExpressionEvaluator;
import org.npsnet.v.services.math.ExpressionEvaluatorFactory;

/**
 * The standard expression evaluator factory.
 *
 * @author Andrzej Kapolka
 */
public class StandardExpressionEvaluatorFactory extends Module implements ExpressionEvaluatorFactory {

    /**
     * Creates and returns a new <code>BooleanExpressionEvaluator</code>.
     *
     * @return the newly created <code>BooleanExpressionEvaluator</code>
     */
    public BooleanExpressionEvaluator newBooleanExpressionEvaluator() {
        return new StandardBooleanExpressionEvaluator();
    }
}
