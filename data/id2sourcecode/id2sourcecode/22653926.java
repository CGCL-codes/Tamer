    public static byte[] copyToByteArray(InputStream _in) throws IOException {
        byte[] buf = new byte[1024];
        int len = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((len = _in.read(buf)) > 0) out.write(buf, 0, len);
        return out.toByteArray();
    }
