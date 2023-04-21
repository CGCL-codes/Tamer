    public static byte[] toByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read = 0;
        while (read != -1) {
            read = stream.read(buffer);
            if (read > 0) {
                baos.write(buffer, 0, read);
            }
        }
        return baos.toByteArray();
    }
