    TestSuiteThread(String tm) {
        jass.runtime.traceAssertion.CommunicationManager.internalAction = true;
        jass.runtime.traceAssertion.Parameter[] jassParameters;
        jassParameters = new jass.runtime.traceAssertion.Parameter[] { new jass.runtime.traceAssertion.Parameter(tm) };
        jass.runtime.traceAssertion.CommunicationManager.internalAction = false;
        jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "TestSuiteThread(java.lang.String)", true), jassParameters);
        if (!((tm.equals("console") || tm.equals("servletlog") || tm.equals("window") || tm.equals("writer")))) throw new jass.runtime.PreconditionException("idebughc.testsuite.TestSuiteThread", "TestSuiteThread(java.lang.String)", 73, "tm_valid");
        this.testMode = tm;
        if (!((testMode == tm))) throw new jass.runtime.PostconditionException("idebughc.testsuite.TestSuiteThread", "TestSuiteThread(java.lang.String)", 78, "testMode_is_valid");
        jass.runtime.traceAssertion.CommunicationManager.internalAction = true;
        jassParameters = new jass.runtime.traceAssertion.Parameter[] {};
        jass.runtime.traceAssertion.CommunicationManager.internalAction = false;
        jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "TestSuiteThread(java.lang.String)", false), jassParameters);
    }
