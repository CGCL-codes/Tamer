    public void testLoggedOutCall() throws Exception {
        JRMPAdaptor adaptor = new JRMPAdaptor();
        JRMPConnector connector = new JRMPConnector();
        try {
            HashMap map = new HashMap();
            String user = "simon";
            char[] password = user.toCharArray();
            map.put(user, password);
            adaptor.setAuthenticator(new UserPasswordAdaptorAuthenticator(map));
            String jndiName = "jrmp";
            adaptor.setJNDIName(jndiName);
            adaptor.setMBeanServer(m_server);
            adaptor.start();
            connector.connect(jndiName, null);
            RemoteMBeanServer server = connector.getRemoteMBeanServer();
            UserPasswordAuthRequest request = new UserPasswordAuthRequest(user, password);
            UserPasswordAuthReply reply = (UserPasswordAuthReply) connector.login(request);
            UserPasswordInvocationContext context = new UserPasswordInvocationContext(reply);
            connector.setInvocationContext(context);
            connector.logout(context);
            try {
                server.getAttribute(new ObjectName("JMImplementation:type=MBeanServerDelegate"), "MBeanServerId");
                fail("Should not be able to call after logging out");
            } catch (SecurityException x) {
            }
        } finally {
            connector.close();
            adaptor.stop();
        }
    }
