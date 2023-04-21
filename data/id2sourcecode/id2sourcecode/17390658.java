    private void copy(ResourceInfo resourceInfo, PrintWriter writer, Range range) throws IOException {
        IOException exception = null;
        InputStream resourceInputStream = resourceInfo.getStream();
        Reader reader = new InputStreamReader(resourceInputStream);
        exception = copyRange(reader, writer, range.start, range.end);
        try {
            reader.close();
        } catch (Throwable t) {
            ;
        }
        if (exception != null) throw exception;
    }
