package unbbayes.prs.prm.cpt;

import java.util.List;
import unbbayes.prs.prm.IAttributeValue;
import unbbayes.prs.prm.IDependencyChain;

/**
 * This class represents an aggregate function "minimum value", which
 * returns the smallest value of a given collection, using compareTo() method
 * of comparables.
 * It considers null values as non-existing (if there is a input having)
 * {@link IAttributeValue#getValue()} == null, it considers as it is not
 * present.
 * @author Shou Matsumoto
 *
 */
public class AggregateFunctionMin extends AbstractAggregateFunction {

    public static final String DEFAULT_NAME = "Min";

    /**
	 * A constructor is visible for subclasses in order to allow
	 * inheritance
	 */
    protected AggregateFunctionMin() {
        this.setName(DEFAULT_NAME);
    }

    /**
	 * Default constructor method
	 * @param relatedChain : the dependency chain where this function must be attached to.
	 * @return a new instance
	 */
    public static AggregateFunctionMin newInstance(IDependencyChain relatedChain) {
        AggregateFunctionMin ret = new AggregateFunctionMin();
        ret.setDependencyChain(relatedChain);
        return ret;
    }

    public IAttributeValue evaluate(List<IAttributeValue> parents) {
        if (parents == null) {
            return null;
        }
        IAttributeValue ret = null;
        for (IAttributeValue attributeValue : parents) {
            if (attributeValue == null || attributeValue.getValue() == null) {
                continue;
            } else if (ret == null) {
                ret = attributeValue;
            } else if (attributeValue.getValue().compareToIgnoreCase(ret.getValue()) < 0) {
                ret = attributeValue;
            }
        }
        return ret;
    }
}
