    public static byte[] getBytes(InputStream is, int max_len) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(max_len);
        for (int i = 0, b = is.read(); b >= 0 && i < max_len; b = is.read(), ++i) baos.write(b);
        return baos.toByteArray();
    }
