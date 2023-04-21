package bpmn.tests;

import bpmn.BpmnFactory;
import bpmn.Message;
import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Message</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class MessageTest extends TestCase {

    /**
	 * The fixture for this Message test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Message fixture = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(MessageTest.class);
    }

    /**
	 * Constructs a new Message test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MessageTest(String name) {
        super(name);
    }

    /**
	 * Sets the fixture for this Message test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void setFixture(Message fixture) {
        this.fixture = fixture;
    }

    /**
	 * Returns the fixture for this Message test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private Message getFixture() {
        return fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    protected void setUp() throws Exception {
        setFixture(BpmnFactory.eINSTANCE.createMessage());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
    protected void tearDown() throws Exception {
        setFixture(null);
    }
}
