    protected void writeLob(Transaction transaction, LobPostProcessing postProcessing) throws OdalPersistencyException {
        if (postProcessing == null || postProcessing.getPersistentObject() == null) {
            return;
        }
        PersistentObject persistentObject = postProcessing.getPersistentObject();
        Record record = persistentObject.record();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        OutputStream outputStream = null;
        Writer writer = null;
        InputStream inputStream = null;
        StringBuffer bindBuffer = new StringBuffer();
        String sql = null;
        try {
            Query query = queryFactory.newQuery();
            query.setSelect(new String[] { record.getColumn(postProcessing.getLobFieldIndex()).getColumnName() });
            query.setFrom(new String[] { record.getTableName() });
            query.addToWhere(SelectQueryBuilderImpl.whereByRecordValues(record, true));
            query.setParameters(primaryKeyValues(record));
            transaction.flush();
            statement = prepareStatement(transaction, query, bindBuffer);
            resultSet = statement.executeQuery();
            boolean hasNext = resultSet.next();
            if (hasNext) {
                Object lobObject = resultSet.getObject(1);
                if (lobObject instanceof Blob) {
                    outputStream = databasePolicy.getBinaryOutputStream((Blob) lobObject);
                    inputStream = (InputStream) postProcessing.getValue();
                    BlobImpl.writeBlob(inputStream, outputStream);
                } else if (lobObject instanceof Clob) {
                    if (postProcessing.getValue() instanceof Reader) {
                        writer = databasePolicy.getCharacterOutputStream((Clob) lobObject);
                        Reader reader = (Reader) postProcessing.getValue();
                        ClobImpl.writeClob(reader, writer);
                    } else if (postProcessing.getValue() instanceof InputStream) {
                        outputStream = databasePolicy.getAsciiOutputStream((Clob) lobObject);
                        inputStream = (InputStream) postProcessing.getValue();
                        ClobImpl.writeClob(inputStream, outputStream);
                    } else {
                        throw new OdalPersistencyException("Expected value of types Reader or InputStream, received: " + postProcessing.getValue());
                    }
                } else {
                    throw new OdalPersistencyException("This is not Blob & not Clob " + lobObject);
                }
            }
        } catch (SQLException e) {
            handleSqlException(e, sql, bindBuffer, transaction);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
            closeAll(transaction, resultSet, statement);
        }
    }
