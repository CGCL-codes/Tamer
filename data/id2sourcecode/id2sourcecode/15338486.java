    public static byte[] decode(String base64) throws IOException {
        Base64InputStream in = new Base64InputStream(new ByteArrayInputStream(base64.getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) (base64.length() / 0.666));
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) out.write(buf, 0, len);
        return out.toByteArray();
    }
