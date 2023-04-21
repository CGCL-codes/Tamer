    public static byte[] readBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        int readedBytes;
        byte[] buf = new byte[1024];
        while ((readedBytes = stream.read(buf)) > 0) {
            b.write(buf, 0, readedBytes);
        }
        b.close();
        return b.toByteArray();
    }
