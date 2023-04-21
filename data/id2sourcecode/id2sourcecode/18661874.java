    public static String toString(InputStream is, int length) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i < length; i++) {
            bos.write(is.read());
        }
        bos.close();
        return bos.toString();
    }
