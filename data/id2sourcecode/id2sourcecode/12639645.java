        super(arg0);
    }

    /**
     * Check that the server is returning the correct error when trying to login using an invalid
     * (i.e. non-existent) user.
     */
    public void testInvalidLogin() {
        try {
            XMPPConnection connection = createConnection();
            connection.connect();
            try {
                connection.login("invaliduser", "invalidpass");
                connection.disconnect();
                fail("Invalid user was able to log into the server");
            } catch (XMPPException e) {
                if (e.getXMPPError() != null) {
                    assertEquals("Incorrect error code while login with an invalid user", 401, e.getXMPPError().getCode());
