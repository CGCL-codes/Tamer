    public void close() {
        try {
            conn.rollback();
            Statement stmt = conn.getStatement();
            stmt.executeUpdate("shutdown");
            stmt.close();
            log.debug("Airlog database closed");
        } catch (Exception e) {
            log.debug(e.toString());
        }
    }
