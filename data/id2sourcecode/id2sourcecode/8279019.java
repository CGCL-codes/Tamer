    public void write() throws WriterException {
        init();
        String currentSQL = null;
        int sqlIndex = 0;
        try {
            connection = null;
            HashMap writerParameters = getWriterParameters();
            if (writerParameters != null) {
                connection = (Connection) writerParameters.get("connection");
            }
            if (connection == null) {
                if (jdbcJar != null) {
                    connection = DBUtils.openConnection(jdbcJar, driver, url, username, password);
                } else {
                    connection = DBUtils.openConnection(driver, url, username, password);
                }
            } else {
                useGivenConnection = true;
            }
            try {
                Statement stmt = connection.createStatement();
                try {
                    if (commitBatchCount != 1) {
                        connection.setAutoCommit(false);
                    }
                    int batchSize = 0;
                    int commitIdx = 0;
                    for (int i = 0; i < getStorage().size(); i++) {
                        String sql = getStorage().get(i);
                        stmt.addBatch(sql);
                        sqlIndex++;
                        currentSQL = sql;
                        if (++batchSize == batchCount) {
                            batchSize = 0;
                            stmt.executeBatch();
                            if (commitBatchCount > 1 && commitBatchCount == ++commitIdx) {
                                connection.commit();
                                commitIdx = 0;
                            }
                            stmt.clearBatch();
                        }
                    }
                    stmt.executeBatch();
                    stmt.clearBatch();
                    if (commitBatchCount != 1) {
                        connection.commit();
                    }
                } finally {
                    stmt.close();
                }
            } finally {
                if (!useGivenConnection) connection.close();
            }
        } catch (StorageException e) {
            throw new WriterException("cannot read data from temporary storage", e);
        } catch (SQLException e) {
            throw new WriterException("cannot write statement to database: " + sqlIndex + "\n" + e.getMessage() + "\n" + currentSQL, e);
        } catch (IOException e) {
            throw new WriterException("cannot find database driver", e);
        } catch (ClassNotFoundException e) {
            throw new WriterException("cannot load database driver", e);
        }
    }
