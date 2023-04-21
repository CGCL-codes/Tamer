    private static StringValue parseAsString(InputStream is, String encoding) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int read = -1;
        while ((read = is.read(buf)) > -1) {
            bos.write(buf, 0, read);
        }
        String s = new String(bos.toByteArray(), encoding);
        return new StringValue(s);
    }
