    public InputStream getInputStream(String uri) throws Exception {
        URL url = new java.net.URL((new File("./")).toURL(), uri);
        return url.openStream();
    }
