    public int executeUpdate(int[] columnIndex) throws SQLException {
        try {
            int num = statement.executeUpdate(getSql(), columnIndex);
            resultSet = statement.getGeneratedKeys();
            return num;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            rollback();
            closeAll();
            throw e;
        }
    }
