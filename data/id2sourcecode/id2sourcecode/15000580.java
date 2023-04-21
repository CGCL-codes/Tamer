    public static void dumpDatabase(final ZipOutputStream output, final Connection connection, final Document challengeDocument) throws SQLException, IOException {
        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            final Charset charset = Charset.forName("UTF-8");
            final OutputStreamWriter outputWriter = new OutputStreamWriter(output, charset);
            output.putNextEntry(new ZipEntry("challenge.xml"));
            XMLUtils.writeXML(challengeDocument, outputWriter, "UTF-8");
            output.closeEntry();
            final DatabaseMetaData metadata = connection.getMetaData();
            rs = metadata.getTables(null, null, "%", new String[] { "TABLE" });
            while (rs.next()) {
                final String tableName = rs.getString("TABLE_NAME");
                dumpTable(output, connection, metadata, outputWriter, tableName.toLowerCase());
            }
            SQLFunctions.close(rs);
        } finally {
            SQLFunctions.close(rs);
            SQLFunctions.close(stmt);
        }
    }
