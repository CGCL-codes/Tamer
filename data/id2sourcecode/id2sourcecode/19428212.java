    private static final void copyData(InputStream input, OutputStream output) throws IOException {
        int len;
        byte[] buff = new byte[16384];
        while ((len = input.read(buff)) > 0) output.write(buff, 0, len);
    }
