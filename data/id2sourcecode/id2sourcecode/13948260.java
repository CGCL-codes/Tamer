    public static String toString(InputStream in) throws IOException {
        assert in != null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[16384];
        int read;
        do {
            read = in.read(b);
            if (read > 0) {
                out.write(b, 0, read);
            }
        } while (read != -1);
        return out.toString();
    }
