    @Test
    public void testCramMd5Login() throws Exception {
        XmppServer xmppServer = createXmppServer();
        xmppServer.setSaslAnonymous(false);
        xmppServer.setSaslPlain(false);
        xmppServer.setSaslCramMd5(true);
        xmppServer.setSaslDigestMd5(false);
        try {
            ConnectionConfiguration configuration = createConfiguration();
            configuration.setSASLAuthenticationEnabled(true);
            configuration.setSecurityMode(SecurityMode.enabled);
            XMPPConnection connection = new XMPPConnection(configuration);
            connection.connect();
            connection.login("unittestusername", "unittestpassword");
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
