package influx.biz.test;

import influx.biz.IBizFactory;
import influx.biz.event.IBizEvent;
import influx.biz.event.impl.BizBroadcastAgent;
import influx.biz.event.impl.BizListEvent;
import influx.biz.event.impl.BizModelEvent;
import influx.biz.event.impl.BizSearchEvent;
import influx.biz.exception.AbortBizEventProcessingException;
import influx.biz.listener.impl.AbstractBizListListener;
import influx.biz.listener.impl.AbstractBizListener;
import influx.biz.phase.IBizListPhase;
import influx.biz.phase.impl.BizListPhase;
import influx.biz.phase.impl.BizModelPhase;
import influx.biz.phase.impl.BizSearchPhase;
import influx.biz.test.dao.ITestDAO;
import influx.biz.test.dao.TestPersistenceFactory;
import influx.biz.test.event.impl.TestExtBizModelEvent;
import influx.biz.test.listener.ITestBizListListener;
import influx.biz.test.listener.ITestBizModelListener;
import influx.biz.test.listener.ITestBizSearchKeywordListener;
import influx.biz.test.listener.ITestBizSearchListener;
import influx.biz.test.model.impl.TestPerson;
import influx.biz.test.phase.ITestSearchPhase;
import influx.biz.test.phase.impl.TestModelPhase;
import influx.biz.test.phase.impl.TestSearchPhase;
import influx.dao.IPersistenceFactory;
import influx.dtc.IEntityDTC;
import influx.dtc.collection.IEntityKeyListDTC;
import influx.dtc.collection.IEntityListDTC;
import influx.dtc.collection.impl.EntityKeyListDTC;
import influx.dtc.collection.impl.EntityListDTC;
import influx.dtc.impl.EntityDTC;
import influx.model.EntityEnableProcessingFlag;
import influx.model.util.EntityUtil;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Common business test case
 * 
 * @author whoover
 */
public class BizTest extends AbstractTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(BizTest.class);

    private static TestPerson testModel = new TestPerson();

    private static IEntityDTC<TestPerson, TestPerson> testEntityDTC;

    private static IEntityListDTC<TestPerson> testEntityListDTC;

    private static IEntityKeyListDTC<TestPerson, TestPerson> testSearchKeywordDTC;

    private static IEntityKeyListDTC<String, TestPerson> testSearchDTC;

    /**
	 * Test setup of persistence/business factories
	 * 
	 * @param getFactoriesByName
	 *            true/false of whether to test factories by name or index
	 */
    @BeforeTest
    @Parameters({ "get-factories-by-name" })
    public void testSetUp(final String getFactoriesByName) {
        if (Boolean.valueOf(getFactoriesByName)) {
            IPersistenceFactory.FACTORY_CONFIGURATION_MANAGER.set(TestPersistenceFactory.class, TestPersistenceFactory.TEST_PERSISTENCE);
            IBizFactory.FACTORY_CONFIGURATION_MANAGER.set(TestBizFactory.class, TestBizFactory.DEFAULT);
        } else {
            IPersistenceFactory.FACTORY_CONFIGURATION_MANAGER.set(TestPersistenceFactory.class, 0);
            IBizFactory.FACTORY_CONFIGURATION_MANAGER.set(TestBizFactory.class, 0);
        }
    }

    /**
	 * Tests the model broadcast
	 */
    @Test
    public void testModelBroadcast() {
        testEntityDTC = new EntityDTC<TestPerson, TestPerson>(BizTest.testModel);
        new BizModelEvent<IEntityDTC<TestPerson, TestPerson>, BizModelPhase>(testEntityDTC, BizModelPhase.FIND_BY_ID, true);
        testEntityDTC.getEventDelegate().addDTCListener(IBizFactory.FACTORY_CONFIGURATION_MANAGER.get().getListenerFactory().getListener(ITestBizModelListener.class));
        LOG.debug("BEGIN -------> BROADCAST");
        BizBroadcastAgent.processBroadcast(BizModelPhase.class, true, testEntityDTC);
        LOG.debug("END -------> BROADCAST");
        Assert.assertNotNull(testEntityDTC.getEntityResult());
    }

    /**
	 * Test list broadcast
	 */
    @Test(dependsOnMethods = { "testModelBroadcast" })
    public void testListBroadcast() {
        EntityUtil.evaluateAnnotatedField(true, EntityEnableProcessingFlag.class, true, BizTest.testModel);
        testEntityListDTC = new EntityListDTC<TestPerson>();
        testEntityListDTC.getCollection().add(BizTest.testModel);
        new BizListEvent<IEntityListDTC<TestPerson>, BizListPhase>(testEntityListDTC, BizListPhase.SAVE, true);
        testEntityListDTC.getEventDelegate().addDTCListener(IBizFactory.FACTORY_CONFIGURATION_MANAGER.get().getListenerFactory().getListener(ITestBizListListener.class));
        LOG.debug("BEGIN -------> BROADCAST");
        BizBroadcastAgent.processBroadcast(BizListPhase.class, true, testEntityListDTC);
        LOG.debug("END -------> BROADCAST");
    }

    /**
	 * Test list broadcast
	 */
    @Test(dependsOnMethods = { "testListBroadcast" })
    public void testListTotalCountBroadcast() {
        EntityUtil.evaluateAnnotatedField(true, EntityEnableProcessingFlag.class, true, BizTest.testModel);
        testEntityListDTC = new EntityListDTC<TestPerson>();
        testEntityListDTC.getCollection().add(BizTest.testModel);
        new BizListEvent<IEntityListDTC<TestPerson>, BizListPhase>(testEntityListDTC, BizListPhase.AUTO_FIND_TOTAL_COUNT, true);
        testEntityListDTC.getEventDelegate().addDTCListener(IBizFactory.FACTORY_CONFIGURATION_MANAGER.get().getListenerFactory().getListener(ITestBizListListener.class));
        LOG.debug("BEGIN -------> BROADCAST");
        BizBroadcastAgent.processBroadcast(BizListPhase.class, true, testEntityListDTC);
        LOG.debug("END -------> BROADCAST");
        Assert.assertTrue(testEntityListDTC.getTotalCount() > 0);
    }

    /**
	 * Test list of entity DTCs broadcast
	 */
    @Test(dependsOnMethods = { "testListTotalCountBroadcast" })
    public void testListOfEntityDTCsBroadcast() {
        testEntityDTC = new EntityDTC<TestPerson, TestPerson>(BizTest.testModel);
        EntityUtil.evaluateAnnotatedField(true, EntityEnableProcessingFlag.class, true, BizTest.testModel);
        EntityUtil.evaluateAnnotatedField(true, EntityEnableProcessingFlag.class, true, testEntityDTC);
        final IEntityListDTC<IEntityDTC<TestPerson, TestPerson>> theTestEntityListDTC = new EntityListDTC<IEntityDTC<TestPerson, TestPerson>>();
        theTestEntityListDTC.getCollection().add(testEntityDTC);
        new BizListEvent<IEntityListDTC<IEntityDTC<TestPerson, TestPerson>>, BizListPhase>(theTestEntityListDTC, BizListPhase.FIND_UNIQUE_BY_EXAMPLE, true);
        theTestEntityListDTC.getEventDelegate().addDTCListener(new AbstractBizListListener<IEntityListDTC<EntityDTC<TestPerson, TestPerson>>, IBizListPhase>() {

            /**
			 * {@inheritDoc}
			 */
            public void process(final IBizEvent<IEntityListDTC<EntityDTC<TestPerson, TestPerson>>, IBizListPhase> bizEvent) throws AbortBizEventProcessingException {
                super.autoProcessEntityDTCList(super.getServiceDAO(ITestDAO.class), bizEvent);
            }
        });
        LOG.debug("BEGIN -------> BROADCAST");
        BizBroadcastAgent.processBroadcast(BizListPhase.class, true, theTestEntityListDTC);
        LOG.debug("END -------> BROADCAST");
        Assert.assertNotNull(theTestEntityListDTC.getCollection().get(0));
        Assert.assertNotNull(theTestEntityListDTC.getCollection().get(0).getEntityResult());
    }

    /**
	 * Test list of entity DTCs broadcast
	 */
    @Test(dependsOnMethods = { "testListOfEntityDTCsBroadcast" })
    public void testSearchBroadcast() {
        testSearchDTC = new EntityKeyListDTC<String, TestPerson>("not-used-key");
        new BizSearchEvent<IEntityKeyListDTC<String, TestPerson>, BizSearchPhase>(testSearchDTC, BizSearchPhase.AUTO_KEYWORD_SEARCH_AND_TOTAL_COUNT, true);
        testSearchDTC.getEventDelegate().addDTCListener(IBizFactory.FACTORY_CONFIGURATION_MANAGER.get().getListenerFactory().getListener(ITestBizSearchKeywordListener.class));
        LOG.debug("BEGIN -------> BROADCAST");
        BizBroadcastAgent.processBroadcast(BizSearchPhase.class, true, testSearchDTC);
        LOG.debug("END -------> BROADCAST");
        Assert.assertNotNull(testSearchDTC.getCollection().get(0));
    }

    /**
	 * Test list of entity DTCs broadcast
	 */
    @Test(dependsOnMethods = { "testSearchBroadcast" })
    public void testSearchTotalCountBroadcast() {
        testSearchDTC = new EntityKeyListDTC<String, TestPerson>("not-used-key");
        new BizSearchEvent<IEntityKeyListDTC<String, TestPerson>, BizSearchPhase>(testSearchDTC, BizSearchPhase.AUTO_FIND_TOTAL_COUNT, true);
        testSearchDTC.getEventDelegate().addDTCListener(IBizFactory.FACTORY_CONFIGURATION_MANAGER.get().getListenerFactory().getListener(ITestBizSearchKeywordListener.class));
        LOG.debug("BEGIN -------> BROADCAST");
        BizBroadcastAgent.processBroadcast(BizSearchPhase.class, true, testSearchDTC);
        LOG.debug("END -------> BROADCAST");
        Assert.assertTrue(testSearchDTC.getTotalCount() > 0);
    }

    /**
	 * Test custom event broadcast
	 */
    @Test(dependsOnMethods = { "testSearchTotalCountBroadcast" })
    public void testCustomEventBroadcast() {
        testEntityDTC = new EntityDTC<TestPerson, TestPerson>(BizTest.testModel);
        new TestExtBizModelEvent(testEntityDTC, TestModelPhase.FIND_BY_ID, true);
        testEntityDTC.getEventDelegate().addDTCListener(IBizFactory.FACTORY_CONFIGURATION_MANAGER.get().getListenerFactory().getListener(ITestBizModelListener.class));
        LOG.debug("BEGIN -------> BROADCAST");
        BizBroadcastAgent.processBroadcast(TestModelPhase.class, true, testEntityDTC);
        LOG.debug("END -------> BROADCAST");
        Assert.assertNotNull(testEntityDTC.getEntityResult());
    }

    /**
	 * Test custom phase broadcast
	 */
    @Test(dependsOnMethods = { "testCustomEventBroadcast" })
    public void testCustomPhaseBroadcast() {
        testSearchKeywordDTC = new EntityKeyListDTC<TestPerson, TestPerson>();
        new BizSearchEvent<IEntityKeyListDTC<TestPerson, TestPerson>, ITestSearchPhase>(testSearchKeywordDTC, TestSearchPhase.FIND_ALL_BY_EXAMPLE, true);
        testSearchKeywordDTC.getEventDelegate().addDTCListener(IBizFactory.FACTORY_CONFIGURATION_MANAGER.get().getListenerFactory().getListener(ITestBizSearchListener.class));
        LOG.debug("BEGIN -------> BROADCAST");
        BizBroadcastAgent.processBroadcast(TestSearchPhase.class, true, testSearchKeywordDTC);
        LOG.debug("END -------> BROADCAST");
        Assert.assertNotNull(testSearchKeywordDTC.getCollection().get(0));
    }

    /**
	 * Test default broadcast instance
	 * 
	 * @throws Exception
	 *             exception
	 */
    @Test(dependsOnMethods = { "testCustomPhaseBroadcast" })
    public void testDefaultBroadcastInstance() throws Exception {
        testEntityDTC = new EntityDTC<TestPerson, TestPerson>(BizTest.testModel);
        new BizModelEvent<IEntityDTC<TestPerson, TestPerson>, BizModelPhase>(testEntityDTC, BizModelPhase.SAVE, true);
        testEntityDTC.getEventDelegate().setBroadcastAgent(new BizBroadcastAgent(BizModelPhase.class));
        LOG.debug("BEGIN -------> BROADCAST");
        testEntityDTC.getEventDelegate().broadcast(true);
        LOG.debug("END -------> BROADCAST");
    }

    /**
	 * Test model phase
	 */
    @Test
    public void testModelPhase() {
        testEntityDTC = new EntityDTC<TestPerson, TestPerson>(BizTest.testModel);
        new BizModelEvent<IEntityDTC<TestPerson, TestPerson>, TestModelPhase>(testEntityDTC, TestModelPhase.FIND_BY_ID, true);
        testEntityDTC.getEventDelegate().addDTCListener(IBizFactory.FACTORY_CONFIGURATION_MANAGER.get().getListenerFactory().getListener(ITestBizModelListener.class));
        LOG.debug("BEGIN -------> BROADCAST");
        BizBroadcastAgent.processBroadcast(TestModelPhase.class, true, testEntityDTC);
        LOG.debug("END -------> BROADCAST");
        Assert.assertNotNull(testEntityDTC.getEntityResult());
        testEntityDTC = new EntityDTC<TestPerson, TestPerson>(BizTest.testModel);
        new BizModelEvent<IEntityDTC<TestPerson, TestPerson>, TestModelPhase>(testEntityDTC, TestModelPhase.FIND_UNIQUE_BY_EXAMPLE, true);
        testEntityDTC.getEventDelegate().addDTCListener(IBizFactory.FACTORY_CONFIGURATION_MANAGER.get().getListenerFactory().getListener(ITestBizModelListener.class));
        LOG.debug("BEGIN -------> BROADCAST");
        BizBroadcastAgent.processBroadcast(TestModelPhase.class, true, testEntityDTC);
        LOG.debug("END -------> BROADCAST");
        Assert.assertNull(testEntityDTC.getEntityResult());
    }

    /**
	 * Test generics
	 */
    @SuppressWarnings("unchecked")
    public void testGeneric() {
        for (Type t : AbstractBizListener.class.getGenericInterfaces()) {
            LOG.debug(t.toString());
        }
        LOG.debug(AbstractBizListener.class.getGenericSuperclass().toString());
        for (TypeVariable v : AbstractBizListener.class.getTypeParameters()) {
            LOG.debug(v.toString());
        }
    }

    /**
	 * Test generics
	 * 
	 * @param t
	 *            the type to print
	 */
    @SuppressWarnings("unchecked")
    private void print(final Type t) {
        if (t instanceof TypeVariable) {
            print((TypeVariable) t);
        } else if (t instanceof WildcardType) {
            print((WildcardType) t);
        } else if (t instanceof ParameterizedType) {
            print((ParameterizedType) t);
        } else if (t instanceof GenericArrayType) {
            print((GenericArrayType) t);
        } else {
            print(t);
        }
    }

    /**
	 * Print
	 * 
	 * @param v
	 *            type variable
	 */
    @SuppressWarnings("unchecked")
    private void print(final TypeVariable v) {
        LOG.debug("");
        LOG.debug("Type variable");
        LOG.debug("Name: " + v.getName());
        LOG.debug("Declaration: " + v.getGenericDeclaration());
        LOG.debug("Bounds:");
        for (Type t : v.getBounds()) {
            print(t);
        }
    }

    /**
	 * Print
	 * 
	 * @param wt
	 *            wildcard type
	 */
    private void print(final WildcardType wt) {
        LOG.debug("");
        LOG.debug("Wildcard type");
        LOG.debug("Lower bounds:");
        for (Type b : wt.getLowerBounds()) {
            print(b);
        }
        LOG.debug("Upper bounds:");
        for (Type b : wt.getUpperBounds()) {
            print(b);
        }
    }

    /**
	 * Print
	 * 
	 * @param pt
	 *            param type
	 */
    private void print(final ParameterizedType pt) {
        LOG.debug("");
        LOG.debug("Parameterized type");
        LOG.debug("Owner: " + pt.getOwnerType());
        LOG.debug("Raw type: " + pt.getRawType());
        for (Type actualType : pt.getActualTypeArguments()) {
            print(actualType);
        }
    }

    /**
	 * Print
	 * 
	 * @param gat
	 *            generic array type
	 */
    private void print(final GenericArrayType gat) {
        LOG.debug("");
        LOG.debug("Generic array type");
        LOG.debug("Type of array: ");
        print(gat.getGenericComponentType());
    }
}
