    private void getResource(String resource) throws MalformedURLException, IOException {
        URL url;
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/" + resource);
        url.openStream();
    }
