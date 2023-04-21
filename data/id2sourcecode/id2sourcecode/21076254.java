    private void writeTimeLogEntries(ZipOutputStream zipOut) throws IOException {
        zipOut.putNextEntry(new ZipEntry(TIME_FILE_NAME));
        TimeLogExporter exp = new TimeLogExporterXMLv1();
        exp.dumpTimeLogEntries(ctx.getTimeLog(), ctx.getData(), filter, zipOut);
        zipOut.closeEntry();
    }
