    public void testInvalidEmail() {
        MSIMConnection connection = new MSIMConnection();
        try {
            connection.connect();
            connection.login("rtg54@hotmail", GOOD_PASSWORD);
        } catch (MSIMException e) {
            assertEquals("260", e.getMessage());
        }
        assertFalse(connection.isConnected());
    }
