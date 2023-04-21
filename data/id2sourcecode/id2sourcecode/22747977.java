    public static long stream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("input stream cannot be null");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("output stream cannot be null");
        }
        long counter = 0;
        int flushCounter = 0;
        byte[] buffer = new byte[4096];
        int read;
        while ((read = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, read);
            counter += read;
            flushCounter += read;
            if (flushCounter >= 8192) {
                outputStream.flush();
                flushCounter = 0;
            }
        }
        outputStream.flush();
        return counter;
    }
