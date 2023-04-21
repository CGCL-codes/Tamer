    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE", justification = "Dynamic based upon tables in the database")
    private static void dumpTable(final ZipOutputStream output, final Connection connection, final DatabaseMetaData metadata, final OutputStreamWriter outputWriter, final String tableName) throws IOException, SQLException {
        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            CSVWriter csvwriter;
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Dumping type information for " + tableName);
            }
            output.putNextEntry(new ZipEntry(tableName + ".types"));
            boolean dumpedTypes = dumpTableTypes(tableName, metadata, outputWriter);
            if (!dumpedTypes) {
                dumpedTypes = dumpTableTypes(tableName.toUpperCase(), metadata, outputWriter);
            }
            if (!dumpedTypes) {
                dumpTableTypes(tableName.toLowerCase(), metadata, outputWriter);
            }
            output.closeEntry();
            output.putNextEntry(new ZipEntry(tableName + ".csv"));
            csvwriter = new CSVWriter(outputWriter);
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            csvwriter.writeAll(rs, true);
            csvwriter.flush();
            output.closeEntry();
            SQLFunctions.close(rs);
            rs = null;
        } finally {
            SQLFunctions.close(rs);
            SQLFunctions.close(stmt);
        }
    }
