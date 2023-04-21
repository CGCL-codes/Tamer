    public ExportStream(java.io.OutputStream wrap) throws IOException {
        theOutput = new java.util.zip.ZipOutputStream(ObfuscatingStream.obfuscate(wrap));
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("export.json");
        theOutput.putNextEntry(zipEntry);
        theOutput.setLevel(9);
    }
