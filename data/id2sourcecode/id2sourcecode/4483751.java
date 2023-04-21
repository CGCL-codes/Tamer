    @Test
    @Ignore("Not implemented yet")
    public void testWrongLoginWithoutSasl() throws Exception {
        XmppServer xmppServer = createXmppServer();
        try {
            ConnectionConfiguration configuration = createConfiguration();
            configuration.setSASLAuthenticationEnabled(false);
            configuration.setSecurityMode(SecurityMode.enabled);
            XMPPConnection connection = new XMPPConnection(configuration);
            connection.connect();
            connection.login("unittestusername", "wrongpassword");
            Assert.fail("Login with wrong password must fail.");
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
