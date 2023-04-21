    public static TelnetSqliteResultSet createResultSet(Socket socket, Reader reader, Writer writer, int rowCount) throws IOException {
        String columnNameListArray = TelnetSqliteConnection.readLine(reader);
        TelnetSqliteResultSetMetaData metaData = null;
        int columnCount = 0;
        ArrayList<ArrayList<String>> resultRow = new ArrayList<ArrayList<String>>();
        if (columnNameListArray.length() > 0 && columnNameListArray.charAt(0) == TelnetSqliteConnection.RESULT_HEADER) {
            String[] columnNameList = StringUtil.split(columnNameListArray.substring(1), ',', '"');
            int[] columnTypeList = new int[columnNameList.length];
            for (int i = 0; i < columnTypeList.length; i++) {
                columnTypeList[i] = Types.VARCHAR;
            }
            columnCount = columnNameList.length;
            while (true) {
                String line = TelnetSqliteConnection.readLine(reader);
                if (line.length() == 0) {
                    break;
                } else if (line.charAt(0) == TelnetSqliteConnection.RESULT_DATA) {
                    String[] csv = split(line.substring(1), ',', '"');
                    ArrayList<String> row = new ArrayList<String>();
                    for (String str : csv) {
                        row.add(str);
                    }
                    while (row.size() < columnCount) {
                        row.add(null);
                    }
                    resultRow.add(row);
                } else {
                    throw new IOException(line);
                }
            }
            metaData = new TelnetSqliteResultSetMetaData(columnNameList, columnTypeList);
        } else if (columnNameListArray.length() == 0) {
            return null;
        } else {
            throw new IOException(columnNameListArray);
        }
        return new TelnetSqliteResultSet(resultRow, metaData, rowCount, columnCount);
    }
