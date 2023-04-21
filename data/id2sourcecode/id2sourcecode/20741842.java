    protected void write(InputStream source, OutputStream destination, int bufferSize) throws IOException {
        if (source == null || destination == null) throw new NullPointerException("Must provide non-null source and destination streams.");
        int read = 0;
        byte[] chunk = new byte[bufferSize];
        while ((read = source.read(chunk)) > 0) destination.write(chunk, 0, read);
    }
