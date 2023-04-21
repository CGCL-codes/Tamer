    protected void tearDown() throws SQLException {
        if (netConn != null) {
            netConn.rollback();
            netConn.createStatement().executeUpdate("SHUTDOWN");
            netConn.close();
            netConn = null;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
            }
        }
        if (server != null && server.getState() != ServerConstants.SERVER_STATE_SHUTDOWN) {
            throw new RuntimeException("Server failed to shut down");
        }
    }
