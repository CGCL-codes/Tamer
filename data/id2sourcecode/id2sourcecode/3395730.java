    public void testSendMessage() {
        MSIMConnection connection = new MSIMConnection();
        connection.connect();
        connection.login(GOOD_EMAIL, GOOD_PASSWORD);
        assertTrue(connection.isConnected());
        InstantMessage message = new InstantMessage();
        message.setTo("6221");
        message.setBody("boo");
        connection.sendPacket(message);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        connection.disconnect();
        assertFalse(connection.isConnected());
    }
