    public static long copy(InputStream aIn, OutputStream aOut, int aBufferSize) throws IOException {
        byte[] buffer = new byte[aBufferSize];
        int read = 0;
        long totalBytes = 0;
        while (-1 != (read = aIn.read(buffer))) {
            aOut.write(buffer, 0, read);
            totalBytes += read;
        }
        return totalBytes;
    }
