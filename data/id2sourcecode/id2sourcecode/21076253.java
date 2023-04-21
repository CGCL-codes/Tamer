    private void writeDefects(ZipOutputStream zipOut) throws IOException {
        zipOut.putNextEntry(new ZipEntry(DEFECT_FILE_NAME));
        DefectExporter exp = new DefectExporterXMLv1();
        exp.dumpDefects(ctx.getHierarchy(), filter, zipOut);
        zipOut.closeEntry();
    }
