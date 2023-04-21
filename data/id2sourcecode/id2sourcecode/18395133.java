    public void setProperties(Properties properties) throws IOException {
        if (dataWritten) {
            throw (new IOException("Backup properties need to be set before any backup data is written"));
        }
        ZipEntry entry = new ZipEntry("backup.properties");
        out.putNextEntry(entry);
        properties.store(out, "Backup properties");
        out.closeEntry();
    }
