    private byte[] readFile(String resource) throws IOException {
        byte[] buf = new byte[4096];
        InputStream is = getClass().getResourceAsStream(resource);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int read = 0;
        while ((read = is.read(buf)) > 0) {
            os.write(buf, 0, read);
        }
        is.close();
        return os.toByteArray();
    }
