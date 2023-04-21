    protected void setUp() {
        String test = System.getProperty("test.name");
        if (test == null) {
            fail("no test specified");
            return;
        }
        java.util.Properties p = new java.util.Properties();
        java.net.URL url = ClassLoader.getSystemResource(test + ".properties");
        try {
            p.load(url.openStream());
        } catch (IOException ie) {
            fail(ie.getMessage());
            return;
        }
        this.impl = p.getProperty("agent_manager");
        try {
            log("loading AgentManager(" + impl + ")");
            this.mgr = (AgentManager) OsidLoader.getManager(osid, impl, new OsidContext(), new java.util.Properties());
            assertNotNull(mgr);
        } catch (OsidException oe) {
            oe.printStackTrace();
            fail("OsidException" + oe.getMessage());
            return;
        }
        String property = p.getProperty("agent_admin");
        if (property.equals("true")) {
            this.supportsAdmin = true;
        } else {
            this.supportsAdmin = false;
        }
        this.agentName = p.getProperty("agent");
    }
