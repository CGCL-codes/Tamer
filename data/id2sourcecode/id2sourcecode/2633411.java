    public File getIndexDir() throws FileNotFoundException, IOException {
        if (this.indexLocation == null) throw new IOException("Index dir not specified.");
        String pathname = this.indexLocation;
        File indexDir = new File(pathname);
        if (!indexDir.exists()) throw new FileNotFoundException("Index Dir not found: " + pathname);
        if (!indexDir.isDirectory()) throw new IOException("Index Dir is not a directory: " + pathname);
        if (!indexDir.canRead() || !indexDir.canWrite()) throw new IOException("Index Dir must have read/write access: " + pathname);
        return indexDir;
    }
