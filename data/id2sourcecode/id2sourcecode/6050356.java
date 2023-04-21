    public void testBadPasswordContext() throws Exception {
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
            UserPasswordAuthReply reply2 = new UserPasswordAuthReply(user, null);
            UserPasswordInvocationContext context2 = new UserPasswordInvocationContext(reply2);
            connector.setInvocationContext(context2);
            try {
                server.getAttribute(new ObjectName("JMImplementation:type=MBeanServerDelegate"), "MBeanServerId");
                fail("Should not be able to call with bad password invocation context");
            } catch (SecurityException x) {
            } finally {
                try {
                    connector.logout(context2);
                    fail("Should not be able to logout");
                } catch (SecurityException c) {
                }
                connector.logout(context1);
            }
        } finally {
            connector.close();
            adaptor.stop();
        }
    }
