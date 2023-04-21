    public void writeAll(ResultSet rs) throws SQLException, IOException, LineMergerException {
        ResultSetMetaData metadata = rs.getMetaData();
        if (includeColumnNames) {
            writeColumnNames(metadata);
        }
        int columnCount = metadata.getColumnCount();
        while (rs.next()) {
            String[] nextLine = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                nextLine[i] = getColumnValue(rs, metadata.getColumnType(i + 1), i + 1);
            }
            writeNext(nextLine);
        }
    }
