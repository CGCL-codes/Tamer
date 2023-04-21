    public static final ByteArrayInputStream readISToBAIS(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buff[] = new byte[1024];
        while (is.available() > 0) {
            int readed = is.read(buff, 0, 1024);
            baos.write(buff, 0, readed);
        }
        buff = null;
        return new ByteArrayInputStream(baos.toByteArray());
    }
