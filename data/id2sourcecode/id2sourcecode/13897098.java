    public PreparedStatement prepareStatement(String sql) throws SQLException {
        prepareConection();
        PreparedStatement stmt = new TelnetSqlitePreparedStatement(socket, reader, writer, sql);
        return stmt;
    }
