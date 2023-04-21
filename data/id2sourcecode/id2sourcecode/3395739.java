    public void testGetRoster() {
        MSIMConnection connection = new MSIMConnection();
        connection.connect();
        connection.login(GOOD_EMAIL, GOOD_PASSWORD);
        assertTrue(connection.isConnected());
        connection.getContactManager().getContacts();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        assertFalse(connection.isConnected());
    }
