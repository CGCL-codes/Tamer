    private void outputRdbDump(ZipOutputStream zipOut) throws IOException, DataSetException, SQLException {
        Assert.Property.requireNotNull(dataSource, "dataSource");
        ZipEntry zipEntry = new ZipEntry(PATH_RDB_DUMP);
        zipOut.putNextEntry(zipEntry);
        RdbUtils.exportAsXml(getJdbcConnection(), TABLES, zipOut);
    }
