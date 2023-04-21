    private void zipContent(OutputStream out) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(out);
        zipOut.putNextEntry(new ZipEntry("TriggerDescContent"));
        writeContentUncompressed(zipOut);
        zipOut.closeEntry();
    }
