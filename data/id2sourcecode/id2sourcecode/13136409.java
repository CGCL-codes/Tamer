    void rollback() throws SQLException {
        checkValid();
        conn.prepareCommand("ROLLBACK TO SAVEPOINT " + getName(name, savepointId), Integer.MAX_VALUE).executeUpdate();
    }
