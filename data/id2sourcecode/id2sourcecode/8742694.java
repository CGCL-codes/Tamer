    private void initialize() throws IOException, JDOMException {
        if (!rootDirectory.isDirectory()) throw new IOException("Path has to point to a directory: " + rootDirectory);
        if (!rootDirectory.exists()) rootDirectory.mkdirs();
        if ((!rootDirectory.canRead()) || (!rootDirectory.canWrite())) throw new IOException("Can not read or write to directory: " + rootDirectory);
        initializeIndex();
    }
