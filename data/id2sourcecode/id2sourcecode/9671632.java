        private void addSpectrum(final StringBuffer readValue, final StringBuffer writeValue, final int dimX, final Timestamp timestamp, final boolean isReadWrite) throws ArchivingException, SQLException {
            dataToInsert.add(new InsertData(readValue, writeValue, dimX, timestamp));
            long currentBulkTime = 0;
            if (currentBulkStartTime == 0) {
                currentBulkStartTime = timestamp.getTime();
            } else {
                currentBulkTime = timestamp.getTime() - currentBulkStartTime;
            }
            currentBulkSize++;
            if (currentBulkSize >= insertBulkSize || currentBulkTime >= insertBulkTime) {
                Connection conn = null;
                PreparedStatement stmt = null;
                try {
                    conn = dbConn.getConnection();
                    stmt = conn.prepareStatement(queryBuffer.toString());
                    int i = 1;
                    for (final InsertData data : dataToInsert) {
                        stmt.setTimestamp(i++, data.getTimestamp());
                        stmt.setInt(i++, data.getDimX());
                        if (data.getReadValue() == null) {
                            stmt.setNull(i++, java.sql.Types.BLOB);
                        } else {
                            stmt.setString(i++, data.getReadValue().toString());
                        }
                        if (isReadWrite) {
                            if (data.getWriteValue() == null) {
                                stmt.setNull(i++, java.sql.Types.BLOB);
                            } else {
                                stmt.setString(i++, data.getWriteValue().toString());
                            }
                        }
                    }
                    stmt.execute();
                } finally {
                    currentBulkStartTime = 0;
                    currentBulkSize = 0;
                    dataToInsert.clear();
                    queryBuffer = new StringBuilder(query);
                    ConnectionCommands.close(stmt);
                    dbConn.closeConnection(conn);
                }
            }
        }
