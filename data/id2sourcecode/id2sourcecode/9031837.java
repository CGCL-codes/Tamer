    public static byte[] bytes(InputStream in) throws IOException {
        if (in == null) throw new IllegalArgumentException("input stream cannot be null");
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
        byte[] bytes = new byte[128];
        for (int x = in.read(bytes); x != -1; x = in.read(bytes)) bout.write(bytes, 0, x);
        return bout.toByteArray();
    }
