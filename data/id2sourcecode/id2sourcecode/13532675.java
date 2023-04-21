    private void newEntry(String resultName) throws IOException {
        if (zipEntry != null) {
            streamWriter.flush();
            zipOutputStream.closeEntry();
        }
        zipEntry = new ZipEntry(resultName);
        zipOutputStream.putNextEntry(zipEntry);
    }
