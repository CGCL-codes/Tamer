    public void startEntry(final String _entryName) throws IOException {
        zipOut_.putNextEntry(new ZipEntry(_entryName));
    }
