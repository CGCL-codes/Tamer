    private void process(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[sChunk];
        int length;
        while ((length = is.read(buffer, 0, sChunk)) != -1) os.write(buffer, 0, length);
    }
