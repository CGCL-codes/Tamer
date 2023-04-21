    public static String stringForStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bytes = new byte[512];
        int readBytes;
        while ((readBytes = in.read(bytes)) > 0) {
            out.write(bytes, 0, readBytes);
        }
        return new String(out.toByteArray());
    }
