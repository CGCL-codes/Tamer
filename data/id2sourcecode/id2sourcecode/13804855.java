    public void stream(final InputStream in, final OutputStream out) throws IOException {
        byte buffer[] = new byte[bufferSize];
        int read = in.read(buffer);
        while (read > -1) {
            out.write(buffer, 0, read);
            read = in.read(buffer);
        }
    }
