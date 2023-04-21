    protected void writeColumnNames(ResultSetMetaData metadata) throws SQLException {
        int columnCount = metadata.getColumnCount();
        String[] nextLine = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            nextLine[i] = metadata.getColumnName(i + 1);
        }
        writeNext(nextLine);
    }
