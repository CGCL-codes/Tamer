    private void parse(InputStream is, int length) throws IOException {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
        byte[] buf = new byte[length];
        int nread;
        while ((nread = is.read(buf, 0, buf.length)) > 0) {
            baos.write(buf, 0, nread);
        }
        bais = new ByteArrayInputStream(baos.toByteArray());
        parse(bais);
    }
