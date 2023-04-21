package unbbayes.prs.prm;

import java.util.List;
import unbbayes.prs.prm.cpt.IAggregateFunction;

/**
 * This is a pointer tracing parents of a random variable, and
 * a path (list of foreign keys) to get to it.
 * @author Shou Matsumoto
 *
 */
public interface IDependencyChain {

    /**
	 * Obtains the aggregation function (e.g. mode, max, min, mean, etc.) 
	 * to be applied by a CPT compiler in order to resolve a set
	 * of parents pointed by this dependency chain.
	 * Usually, if this method returns a non-null value,
	 * it means that at least one Fk in {@link #getForeignKeyChain()}
	 * is a inverse reference.
	 * @return
	 */
    public IAggregateFunction getAggregateFunction();

    /**
	 * Sets the aggregation function (e.g. mode, max, min, mean, etc.) 
	 * to be applied by a CPT compiler in order to resolve a set
	 * of parents pointed by this dependency chain.
	 * Usually, if this method sets a non-null value,
	 * it means that at least one Fk in {@link #getForeignKeyChain()}
	 * is a inverse reference.
	 * @param aggregateFunction
	 */
    public void setAggregateFunction(IAggregateFunction aggregateFunction);

    /**
	 * Obtains the entity from where this dependency chain starts.
	 * We do not pointer directly to a {@link IAttributeDescriptor}
	 * because all probability-specific information must be centered
	 * in {@link IPRMDependency}.
	 * @return
	 */
    public IPRMDependency getDependencyFrom();

    /**
	 * Sets the entity from where this dependency chain starts.
	 * We do not pointer directly to a {@link IAttributeDescriptor}
	 * because all probability-specific information must be centered
	 * in {@link IPRMDependency}.
	 * @param from
	 */
    public void setDependencyFrom(IPRMDependency from);

    /**
	 * Obtains the final target attribute where this dependency chain points to.
	 * We do not pointer directly to a {@link IAttributeDescriptor}
	 * because all probability-specific information must be centered
	 * in {@link IPRMDependency}.
	 * @return
	 */
    public IPRMDependency getDependencyTo();

    /**
	 * Sets the final target attribute where this dependency chain points to.
	 * We do not pointer directly to a {@link IAttributeDescriptor}
	 * because all probability-specific information must be centered
	 * in {@link IPRMDependency}.
	 * @param to
	 */
    public void setDependencyTo(IPRMDependency to);

    /**
	 * Obtains the path from {@link #getDependencyFrom()} to
	 * {@link #getDependencyTo()}, passing through 
	 * several foreign key chain.
	 * @return
	 */
    public List<IForeignKey> getForeignKeyChain();

    /**
	 * Sets the path from {@link #getDependencyFrom()} to
	 * {@link #getDependencyTo()}, passing through 
	 * several foreign key chain.
	 * @param foreignKeys
	 */
    public void setForeignKeyChain(List<IForeignKey> foreignKeys);

    /**
	 * Verifies if a {@link IForeignKey} within
	 * {@link #getForeignKeyChain()} is marked as
	 * inverse FK. A FK is inverse if it is a part
	 * of {@link PRMClass#getIncomingForeignKeys()}
	 * @param fk
	 * @return
	 */
    public boolean isInverseForeignKey(IForeignKey fk);

    /**
	 * If isInverse==true, mark fk as inverse foreign key. If
	 * isInverse==false, mark fk as normal foreign key. These informations
	 * are important in order to solve object references, because
	 * if there is self reference (class containing references to itself), it is
	 * difficult to know if we are declaring dependence from object A to B (of same class) or
	 * it is from B to A.
	 * Note that {@link #getForeignKeyChain()} must contain fk.
	 * @param fk
	 * @param isInverse
	 * @see #isInverseForeignKey(IForeignKey)
	 */
    public void markAsInverseForeignKey(IForeignKey fk, boolean isInverse);
}
