    private int getStream(final InputStream is, final int contentLength, OutputStream out) throws IOException {
        int progress = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int readCount;
        while ((readCount = is.read(buffer)) > 0) {
            String bufStr = new String(buffer);
            out.write(buffer, 0, readCount);
            progress += readCount;
            fireIOEvent(new IOEvent(this, contentLength, progress, null, null));
        }
        out.flush();
        out.close();
        out = null;
        return progress;
    }
