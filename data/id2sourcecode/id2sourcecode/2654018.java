    protected void copy(CacheEntry cacheEntry, InputStream is, PrintWriter writer) throws IOException {
        IOException exception = null;
        InputStream resourceInputStream = null;
        if (cacheEntry.resource != null) {
            resourceInputStream = cacheEntry.resource.streamContent();
        } else {
            resourceInputStream = is;
        }
        Reader reader;
        if (fileEncoding == null) {
            reader = new InputStreamReader(resourceInputStream);
        } else {
            reader = new InputStreamReader(resourceInputStream, fileEncoding);
        }
        exception = copyRange(reader, writer);
        reader.close();
        if (exception != null) throw exception;
    }
