    public void testNotSuccesfulSecureCall() throws Exception {
        JRMPAdaptor adaptor = new JRMPAdaptor();
        JRMPConnector connector = new JRMPConnector();
        try {
            adaptor.setAuthenticator(new CerberoAdaptorAuthenticator());
            String jndiName = "jrmp";
            adaptor.setJNDIName(jndiName);
            adaptor.setMBeanServer(m_server);
            adaptor.start();
            connector.connect(jndiName, null);
            RemoteMBeanServer server = connector.getRemoteMBeanServer();
            String user = "guest";
            String address = InetAddress.getLocalHost().getHostAddress();
            char[] password = user.toCharArray();
            CerberoAuthRequest request = new CerberoAuthRequest(user, address);
            CerberoAuthReply reply = (CerberoAuthReply) connector.login(request);
            PasswordEncryptedObject ticket = reply.getLoginTicket();
            LoginTicket login = (LoginTicket) ticket.decrypt(password);
            char[] key = login.getKey();
            AuthenticatorTicket auth = new AuthenticatorTicket(user, address, 15 * 1000);
            PasswordEncryptedObject authenticator = new PasswordEncryptedObject(auth, key);
            ServiceTicket serviceTicket = new ServiceTicket(authenticator, login.getGrantingTicket());
            CerberoInvocationContext context = new CerberoInvocationContext(serviceTicket);
            connector.setInvocationContext(context);
            try {
                server.getAttribute(new ObjectName("JMImplementation:type=MBeanServerDelegate"), "MBeanServerId");
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
