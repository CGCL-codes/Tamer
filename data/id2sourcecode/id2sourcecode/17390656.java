    private void copy(ResourceInfo resourceInfo, PrintWriter writer) throws IOException {
        IOException exception = null;
        InputStream resourceInputStream = resourceInfo.getStream();
        Reader reader = new InputStreamReader(resourceInputStream);
        exception = copyRange(reader, writer);
        try {
            reader.close();
        } catch (Throwable t) {
            ;
        }
        if (exception != null) throw exception;
    }
