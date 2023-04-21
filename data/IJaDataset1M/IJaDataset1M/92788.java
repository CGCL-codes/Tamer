package com.firescrum.testmodule.dao;

import java.util.List;
import com.firescrum.core.model.Product;
import com.firescrum.infrastructure.dao.IBaseDao;
import com.firescrum.testmodule.model.TestPlan;

/**
 * IDaoTestPlan is the interface definition of the data access layer of
 * the entity {@link TestPlan}.
 * @author Leopoldo Ferreira.
 */
public interface IDaoTestPlan extends IBaseDao<TestPlan> {

    /**
     * Retrieves true if there is a Test Plan with the name like the
     * passed one. Retrieves false if not.
     *
     * @param testPlanName the name of the Test Plan.
     * @return a {@link Boolean} if the {@link String} have the
     * characters in the same order than the parameter. It is not
     * case sensitive.
     */
    boolean isRepeatedName(String testSuiteName, Product product);

    /**
     * Retrieves all {@link TestPlan} of {@link Product}.
     *
     * @param product the {@link Product}
     * @return a {@link Collection} of {@link TestPlan}
     */
    List<TestPlan> getTestPlansOfProduct(Product product);
}
