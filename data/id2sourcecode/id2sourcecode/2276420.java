    protected Object readNext() {
        try {
            String channelId = null;
            Set<String> keys = null;
            String schemaName = null;
            String catalogName = null;
            String[] parsedOldData = null;
            long bytesRead = 0;
            while (csvReader.readRecord()) {
                String[] tokens = csvReader.getValues();
                if (batch == null) {
                    bytesRead += csvReader.getRawRecord().length();
                } else {
                    batch.incrementReadByteCount(csvReader.getRawRecord().length() + bytesRead);
                    bytesRead = 0;
                }
                if (tokens[0].equals(CsvConstants.BATCH)) {
                    return new Batch(Long.parseLong(tokens[1]), channelId);
                } else if (tokens[0].equals(CsvConstants.NODEID)) {
                    this.context.setSourceNodeId(tokens[1]);
                } else if (tokens[0].equals(CsvConstants.BINARY)) {
                    context.setBinaryEncoding(BinaryEncoding.valueOf(tokens[1]));
                } else if (tokens[0].equals(CsvConstants.CHANNEL)) {
                    channelId = tokens[1];
                } else if (tokens[0].equals(CsvConstants.SCHEMA)) {
                    schemaName = StringUtils.isBlank(tokens[1]) ? null : tokens[1];
                } else if (tokens[0].equals(CsvConstants.CATALOG)) {
                    catalogName = StringUtils.isBlank(tokens[1]) ? null : tokens[1];
                } else if (tokens[0].equals(CsvConstants.TABLE)) {
                    String tableName = tokens[1];
                    table = tables.get(Table.getFullyQualifiedTableName(tableName, schemaName, catalogName, ""));
                    if (table != null) {
                        return table;
                    } else {
                        table = new Table(catalogName, schemaName, tableName);
                    }
                } else if (tokens[0].equals(CsvConstants.KEYS)) {
                    if (keys == null) {
                        keys = new HashSet<String>(tokens.length);
                    }
                    for (int i = 1; i < tokens.length; i++) {
                        keys.add(tokens[i]);
                    }
                } else if (tokens[0].equals(CsvConstants.COLUMNS)) {
                    for (int i = 1; i < tokens.length; i++) {
                        Column column = new Column(tokens[i], keys != null && keys.contains(tokens[i]));
                        table.addColumn(column);
                    }
                    tables.put(table.getFullyQualifiedTableName(""), table);
                    return table;
                } else if (tokens[0].equals(CsvConstants.COMMIT)) {
                    return null;
                } else if (tokens[0].equals(CsvConstants.INSERT)) {
                    Data data = new Data();
                    data.setEventType(DataEventType.INSERT);
                    data.setChannelId(batch.getChannelId());
                    data.putParsedRowData(ArrayUtils.subarray(tokens, 1, tokens.length));
                    return data;
                } else if (tokens[0].equals(CsvConstants.OLD)) {
                    parsedOldData = ArrayUtils.subarray(tokens, 1, tokens.length);
                } else if (tokens[0].equals(CsvConstants.UPDATE)) {
                    Data data = new Data();
                    data.setEventType(DataEventType.UPDATE);
                    data.setChannelId(batch.getChannelId());
                    data.putParsedRowData(ArrayUtils.subarray(tokens, 1, table.getColumnCount() + 1));
                    data.putParsedPkData(ArrayUtils.subarray(tokens, table.getColumnCount() + 1, tokens.length));
                    data.putParsedOldData(parsedOldData);
                    return data;
                } else if (tokens[0].equals(CsvConstants.DELETE)) {
                    Data data = new Data();
                    data.setEventType(DataEventType.DELETE);
                    data.setChannelId(batch.getChannelId());
                    data.putParsedPkData(ArrayUtils.subarray(tokens, 1, tokens.length));
                    data.putParsedOldData(parsedOldData);
                    return data;
                } else if (tokens[0].equals(CsvConstants.SQL)) {
                    Data data = new Data();
                    data.setEventType(DataEventType.SQL);
                    data.setChannelId(batch.getChannelId());
                    data.setRowData(tokens[1]);
                    return data;
                } else {
                }
            }
        } catch (IOException ex) {
            throw new IoException(ex);
        }
        return null;
    }
