    public static String inputStream2String(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = in.read(b)) != -1) out.write(b, 0, len);
        return new String(out.toByteArray());
    }
