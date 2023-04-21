    @Test
    public void testDigestMd5WrongLogin() throws Exception {
        XmppServer xmppServer = createXmppServer();
        xmppServer.setSaslAnonymous(false);
        xmppServer.setSaslPlain(false);
        xmppServer.setSaslCramMd5(false);
        xmppServer.setSaslDigestMd5(true);
        try {
            ConnectionConfiguration configuration = createConfiguration();
            configuration.setSASLAuthenticationEnabled(true);
            configuration.setSecurityMode(SecurityMode.enabled);
            XMPPConnection connection = new XMPPConnection(configuration);
            connection.connect();
            try {
                connection.login("unittestusername", "wrongpassword");
                Assert.fail("Login with wrong password must fail.");
            } catch (XMPPException e) {
                if (!e.toString().equals("SASL authentication failed using mechanism DIGEST-MD5: ")) {
                    throw e;
                }
            }
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
