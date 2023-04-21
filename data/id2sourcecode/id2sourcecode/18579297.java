    protected void importData(Map keys) throws BuildException {
        log("Importing Data...");
        try {
            try {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("TRUNCATE TABLE " + getTable());
                stmt.close();
                for (Iterator i = keys.keySet().iterator(); i.hasNext(); ) {
                    String key = (String) i.next();
                    File file = (File) keys.get(key);
                    stmt = connection.createStatement();
                    StringBuffer sqlInsert = new StringBuffer();
                    sqlInsert.append("INSERT INTO ").append(getTable());
                    sqlInsert.append(" (");
                    sqlInsert.append(KEY_NAME).append(',');
                    sqlInsert.append(TIMESTAMP_NAME).append(',').append(XML_NAME);
                    sqlInsert.append(") VALUES (");
                    sqlInsert.append("'").append(key).append("'");
                    sqlInsert.append(",sysdate, empty_clob())");
                    stmt.executeUpdate(sqlInsert.toString());
                    connection.setAutoCommit(false);
                    StringBuffer sqlSelect = new StringBuffer();
                    sqlSelect.append("SELECT ").append(XML_NAME);
                    sqlSelect.append(" FROM ").append(getTable());
                    sqlSelect.append(" WHERE ").append(KEY_NAME);
                    sqlSelect.append("='").append(key).append("'");
                    sqlSelect.append(" FOR UPDATE");
                    ResultSet rs = connection.createStatement().executeQuery(sqlSelect.toString());
                    if (rs.next()) {
                        FileInputStream in = new FileInputStream(file);
                        oracle.sql.CLOB clob = ((OracleResultSet) rs).getCLOB(1);
                        OutputStream out = clob.getAsciiOutputStream();
                        byte[] buff = new byte[clob.getBufferSize()];
                        int length = -1;
                        while ((length = in.read(buff)) != -1) out.write(buff, 0, length);
                        in.close();
                        out.close();
                    }
                }
            } finally {
                if (connection != null) connection.commit();
            }
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
