    protected void readwriteStreams(InputStream in, OutputStream out) throws IOException {
        int numRead;
        byte[] buffer = new byte[8 * 1024];
        while ((numRead = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, numRead);
        }
    }
