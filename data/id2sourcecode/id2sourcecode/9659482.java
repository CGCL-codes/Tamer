    public static byte[] loadProbe(InputStream in, int buffSize) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buff = new byte[buffSize];
        int read = in.read(buff);
        if (read > 0) {
            bout.write(buff, 0, read);
        }
        return bout.toByteArray();
    }
