    public static byte[] bytesFromInputStream(InputStream in) throws IOException {
        if (in == null) throw new IllegalArgumentException("null input stream");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int read = -1;
        byte[] buf = new byte[1024 * 50];
        while ((read = in.read(buf)) != -1) {
            bout.write(buf, 0, read);
        }
        return bout.toByteArray();
    }
