    public byte[] readResource(String resource) throws IOException {
        InputStream is = getClass().getResourceAsStream(resource);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int readed;
        while ((readed = is.read(buffer)) > 0) {
            bos.write(buffer, 0, readed);
        }
        is.close();
        bos.close();
        return bos.toByteArray();
    }
