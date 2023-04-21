    @Override
    public void close() throws IOException {
        logger.debug("Closing wrapped stream - passing through remainder");
        byte[] buf = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = wrapped.read(buf)) > -1) {
            out.write(buf, 0, read);
        }
        logger.debug("Now actually closing the wrapped stream and copied " + "stream");
        wrapped.close();
        out.close();
    }
