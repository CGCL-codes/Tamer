    public void testSuccesfulSecureCall() throws Exception {
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
            try {
                String id = (String) server.getAttribute(new ObjectName("JMImplementation:type=MBeanServerDelegate"), "MBeanServerId");
                System.out.println("ID = " + id);
            } finally {
                connector.logout(context);
            }
        } finally {
            connector.close();
            adaptor.stop();
        }
    }
