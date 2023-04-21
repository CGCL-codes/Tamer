    public void startMetadata() throws IOException {
        ZipEntry metaEntry = new ZipEntry("metadata.xml");
        zos.putNextEntry(metaEntry);
    }
