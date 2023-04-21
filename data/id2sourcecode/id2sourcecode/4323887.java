    protected String readResource(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[512];
        int read;
        while ((read = in.read(buff)) != -1) {
            baos.write(buff, 0, read);
        }
        String str = baos.toString();
        return str;
    }
