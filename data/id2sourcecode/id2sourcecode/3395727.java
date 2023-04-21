    public void testGoodLogin() {
        MSIMConnection connection = new MSIMConnection();
        connection.connect();
        connection.login(GOOD_EMAIL, GOOD_PASSWORD);
        assertTrue(connection.isConnected());
        connection.disconnect();
        assertFalse(connection.isConnected());
    }
