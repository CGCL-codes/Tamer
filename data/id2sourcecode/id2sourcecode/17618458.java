    public void transform(final File arc, final File warc, final boolean force) throws IOException {
        FileUtils.isReadable(arc);
        if (warc.exists() && !force) {
            throw new IOException("Target WARC already exists. " + "Will not overwrite.");
        }
        ARCReader reader = ARCReaderFactory.get(arc, false, 0);
        transform(reader, warc);
    }
