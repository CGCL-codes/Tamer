    public static byte[] readByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bs = new byte[1024 * 64];
        int read = 0;
        while ((read = inputStream.read(bs)) != -1) {
            baos.write(bs, 0, read);
        }
        return baos.toByteArray();
    }
