                fail(e.getMessage());
            }
            conn1.disconnect();
            conn2.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Check that the server does not allow to log in without specifying a resource.
     */
    public void testLoginWithNoResource() {
        try {
            XMPPConnection conn = createConnection();
            conn.connect();
            try {
                conn.getAccountManager().createAccount("user_1", "user_1");
            } catch (XMPPException e) {
                if (e.getXMPPError().getCode() != 409) {
                    throw e;
                }
