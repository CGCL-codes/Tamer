    public void testBadpassword() {
        MSIMConnection connection = new MSIMConnection();
        try {
            connection.connect();
            connection.login(GOOD_EMAIL, "badpassword");
        } catch (MSIMException e) {
            assertEquals("260", e.getMessage());
        }
        assertFalse(connection.isConnected());
    }
