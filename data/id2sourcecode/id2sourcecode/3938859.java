    protected void executeLobHack() throws SQLException {
        try {
            if (lobs != null && lobs.size() > 0 && parsedStatement.columns != null) {
                Connection connection = getConnection();
                StringBuffer sql = new StringBuffer(100);
                sql.append("select ");
                for (Iterator i = lobs.iterator(); i.hasNext(); ) {
                    OracleHackedLob lob = (OracleHackedLob) i.next();
                    sql.append(lob.column.name);
                    if (i.hasNext()) {
                        sql.append(",");
                    }
                }
                sql.append(" from ");
                sql.append(parsedStatement.table);
                StatementColumn column = parsedStatement.getIdColumn(connection);
                sql.append(" where ");
                sql.append(column.name);
                sql.append("=?");
                PreparedStatement ps = connection.prepareStatement(sql.toString());
                Object id = parsedStatement.getId();
                if (id instanceof Long) {
                    ps.setLong(1, ((Long) id).longValue());
                } else if (id instanceof Integer) {
                    ps.setLong(1, ((Integer) id).intValue());
                } else if (id instanceof String) {
                    ps.setString(1, (String) id);
                }
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int index = 1;
                    for (Iterator i = lobs.iterator(); i.hasNext(); index++) {
                        OracleHackedLob lob = (OracleHackedLob) i.next();
                        if (lob.data instanceof Clob) {
                            oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(index);
                            Writer writer = clob.getCharacterOutputStream();
                            Reader reader = ((Clob) lob.data).getCharacterStream();
                            FileUtil.copyFile(reader, writer);
                            reader.close();
                            writer.close();
                        } else if (lob.data instanceof Blob) {
                            oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(index);
                            OutputStream output = blob.getBinaryOutputStream();
                            InputStream input = ((Blob) lob.data).getBinaryStream();
                            FileUtil.copyFile(input, output);
                            input.close();
                            output.close();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
