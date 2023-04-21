    public static long copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFF_SIZE];
        int readCount = 0;
        long written = 0;
        while ((readCount = input.read(buffer)) != -1) {
            output.write(buffer, 0, readCount);
            if (readCount > 0) {
                written += readCount;
            }
        }
        return written;
    }
