    public static void writeTo(InputStream is, OutputStream os) throws IOException {
        int readed;
        byte buf[] = new byte[1024 * 1024];
        while ((readed = is.read(buf, 0, buf.length)) > 0) {
            os.write(buf, 0, readed);
        }
    }
