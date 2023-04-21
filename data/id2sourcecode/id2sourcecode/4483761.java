    @Ignore
    public void testRoster() throws Exception {
        XmppServer xmppServer = createXmppServer();
        try {
            ConnectionConfiguration configuration = createConfiguration();
            configuration.setSASLAuthenticationEnabled(true);
            configuration.setSecurityMode(SecurityMode.enabled);
            XMPPConnection connection = new XMPPConnection(configuration);
            connection.connect();
            connection.login("unittestusername", "unittestpassword");
            connection.getRoster();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            Thread.sleep(SLEEP_AFTER_EACH_TEST);
            xmppServer.shutdown();
            port++;
        }
    }
