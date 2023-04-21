    public void testNotSuccesfulSecureCall() throws Exception {
        JRMPAdaptor adaptor = new JRMPAdaptor();
        JRMPConnector connector = new JRMPConnector();
        try {
            HashMap map = new HashMap();
            String user = "simon";
            char[] password = user.toCharArray();
            map.put(user, password);
            String anotherUser = "another";
            map.put(anotherUser, password);
            adaptor.setAuthenticator(new UserPasswordAdaptorAuthenticator(map));
            String jndiName = "jrmp";
            adaptor.setJNDIName(jndiName);
            adaptor.setMBeanServer(m_server);
            adaptor.start();
            connector.connect(jndiName, null);
            RemoteMBeanServer server = connector.getRemoteMBeanServer();
            UserPasswordAuthRequest request = new UserPasswordAuthRequest(anotherUser, password);
            UserPasswordAuthReply reply = (UserPasswordAuthReply) connector.login(request);
            UserPasswordInvocationContext context = new UserPasswordInvocationContext(reply);
            connector.setInvocationContext(context);
            try {
                String id = (String) server.getAttribute(new ObjectName("JMImplementation:type=MBeanServerDelegate"), "MBeanServerId");
                fail("User has not the right permissions");
            } catch (SecurityException x) {
            } finally {
                connector.logout(context);
            }
        } finally {
            connector.close();
            adaptor.stop();
        }
    }
