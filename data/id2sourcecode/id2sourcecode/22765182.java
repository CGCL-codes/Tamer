    private InputStream getJarAsStream(String libjar_name) throws Exception {
        return new ByteArrayInputStream(getBytes(urls[0].openStream(), libjar_name));
    }
