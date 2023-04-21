    public void testBadUserContext() throws Exception {
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
            UserPasswordAuthReply reply1 = (UserPasswordAuthReply) connector.login(request);
            UserPasswordInvocationContext context1 = new UserPasswordInvocationContext(reply1);
            UserPasswordAuthReply reply2 = new UserPasswordAuthReply(null, password);
            UserPasswordInvocationContext context2 = new UserPasswordInvocationContext(reply2);
            connector.setInvocationContext(context2);
            try {
                server.getAttribute(new ObjectName("JMImplementation:type=MBeanServerDelegate"), "MBeanServerId");
                fail("Should not be able to call with bad user invocation context");
            } catch (SecurityException x) {
            } finally {
                try {
                    connector.logout(context2);
                    fail("Should not be able to logout");
                } catch (SecurityException x) {
                }
                connector.logout(context1);
            }
        } finally {
            connector.close();
            adaptor.stop();
        }
    }
