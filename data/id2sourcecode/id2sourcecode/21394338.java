    public int executeUpdate() throws SQLException {
        try {
            int rows = statement.executeUpdate();
            return rows;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            rollback();
            closeAll();
            throw e;
        }
    }
