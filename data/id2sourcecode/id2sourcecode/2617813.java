    public void close() throws SQLException {
        synchronized (database) {
            SQLException closeException = null;
            if (!database.isReadOnly()) {
                try {
                    flush();
                } catch (SQLException e) {
                    closeException = e;
                }
            }
            cache.clear();
            if (file != null) {
                file.closeSilently();
                file = null;
            }
            if (closeException != null) {
                throw closeException;
            }
            readCount = writeCount = 0;
        }
    }
