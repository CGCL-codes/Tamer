    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BYTE_COPY_BUFFER_SIZE];
        int numBytes;
        while ((numBytes = in.read(buffer)) > 0) out.write(buffer, 0, numBytes);
    }
