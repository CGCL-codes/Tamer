    protected void copy(CacheEntry cacheEntry, PrintWriter writer, Range range) throws IOException {
        IOException exception = null;
        InputStream resourceInputStream = cacheEntry.resource.streamContent();
        Reader reader;
        if (fileEncoding == null) {
            reader = new InputStreamReader(resourceInputStream);
        } else {
            reader = new InputStreamReader(resourceInputStream, fileEncoding);
        }
        exception = copyRange(reader, writer, range.start, range.end);
        reader.close();
        if (exception != null) throw exception;
    }
