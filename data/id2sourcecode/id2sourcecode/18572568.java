    public static void writeExportData(String json, java.io.OutputStream os) throws java.io.IOException {
        java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(ObfuscatingStream.obfuscate(os));
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("export.json");
        zos.putNextEntry(zipEntry);
        zos.setLevel(9);
        java.io.Writer writer = new java.io.OutputStreamWriter(zos);
        writer.write(json, 0, json.length());
        writer.flush();
        writer.close();
        zos.finish();
        zos.close();
    }
