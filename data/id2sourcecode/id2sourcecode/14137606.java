    public void addEntry(String entryName, long size) throws IOException {
        super.addEntry(entryName, size);
        ZipEntry entry = new ZipEntry(entryName);
        ((ZipOutputStream) zout).putNextEntry(entry);
    }
