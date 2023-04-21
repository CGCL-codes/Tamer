    public static Test suite() throws Exception {
        java.net.URL url = ClassLoader.getSystemResource("host0.jndi.properties");
        java.util.Properties host0JndiProps = new java.util.Properties();
        host0JndiProps.load(url.openStream());
        java.util.Properties systemProps = System.getProperties();
        systemProps.putAll(host0JndiProps);
        System.setProperties(systemProps);
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(T03MixedDTMOTSUnitTestCase.class));
        TestSetup wrapper = new JBossTestSetup(suite) {

            protected void setUp() throws Exception {
                super.setUp();
                deploy("dtmotsfrontend.jar");
            }

            protected void tearDown() throws Exception {
                undeploy("dtmotsfrontend.jar");
                super.tearDown();
            }
        };
        return wrapper;
    }
