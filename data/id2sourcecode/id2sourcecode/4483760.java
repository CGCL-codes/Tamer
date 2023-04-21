    @Test
    public void testSimpleTextMessage() throws Exception {
        XmppServer xmppServer = createXmppServer();
        try {
            ConnectionConfiguration configuration = createConfiguration();
            configuration.setSASLAuthenticationEnabled(true);
            configuration.setSecurityMode(SecurityMode.enabled);
            XMPPConnection connection = new XMPPConnection(configuration);
            connection.connect();
            connection.login("unittestusername", "unittestpassword");
            Message message = new Message();
            message.setType(Type.normal);
            message.setThread("thread");
            message.setFrom("from");
            message.setTo("to");
            message.setSubject("Thread subject");
            message.setBody("Just a simple text message.");
            System.out.println(message.toXML());
            connection.sendPacket(message);
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
