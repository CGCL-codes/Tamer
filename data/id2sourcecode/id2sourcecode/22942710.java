    public void execute(OutputStream outputStream) throws IOException, SQLException {
        if (connection == null) {
            throw new NullPointerException("You must set a connection");
        }
        SpreadsheetWriter writer = new SpreadsheetWriter();
        for (String sheetName : sheetDefinitions.keySet()) {
            log.info("Creating sheet " + sheetName + "...");
            writer.createSheet(sheetName);
            log.info("Executing Query...");
            ResultSet resultSet = connection.prepareStatement(sheetDefinitions.get(sheetName)).executeQuery();
            List<String> headers = new ArrayList<String>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                headers.add(metaData.getColumnName(i + 1));
            }
            log.info("Writing Headers...");
            writer.setHeaders(sheetName, headers.toArray(new String[headers.size()]));
            while (resultSet.next()) {
                List<Object> dataRow = new ArrayList<Object>();
                for (String header : headers) {
                    dataRow.add(resultSet.getObject(header));
                }
                log.info("Adding Data Row...");
                writer.addDataRow(sheetName, dataRow.toArray(new Object[dataRow.size()]));
            }
        }
        writer.write(outputStream);
    }
