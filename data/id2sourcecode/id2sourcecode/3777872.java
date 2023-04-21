    @Override
    protected void putEntry(Object os, Object entry) throws IOException {
        ((ZipOutputStream) os).putNextEntry((ZipEntry) entry);
    }
