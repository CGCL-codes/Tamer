    public ResultSet executeQuery(String sql) throws SQLException {
        DriverManager.println("WsvJdbc - WsvStatement:executeQuery() - sql= " + sql);
        WsvSqlParser parser;
        parser = new WsvSqlParser();
        try {
            parser.parse(sql, connection);
        } catch (Exception e) {
            throw new SQLException("Syntax Error. " + e.getMessage());
        }
        java.net.URLConnection urlConnection;
        URL fileURL;
        String path;
        fileURL = null;
        path = connection.getPath() + parser.getTableName() + connection.getExtension();
        try {
            fileURL = UrlUtils.verifyAndSetUrl(path);
            urlConnection = fileURL.openConnection();
        } catch (java.io.IOException ioe) {
            throw new SQLException("Cannot open data file '" + fileURL.toExternalForm() + "'  ! Message was: " + ioe);
        }
        try {
            urlConnection.getInputStream();
        } catch (java.io.IOException ioe) {
            throw new SQLException("Data file '" + fileURL.toExternalForm() + "'  not readable ! Message was: " + ioe);
        }
        WsvReader reader;
        try {
            reader = getReader(parser, fileURL);
        } catch (Exception e) {
            throw new SQLException("Error reading data file. Message was: " + e + " (maybe the file is empty!) ");
        }
        WsvResultSet resultSet;
        resultSet = getResultSet(parser, reader);
        resultSets.add(resultSet);
        return resultSet;
    }
