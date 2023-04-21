    public Reader getReader(String host, int port, String path) throws IOException, MalformedURLException {
        URL url = new URL("http://" + host + ":" + port + "/" + path);
        URLConnection connection = url.openConnection();
        return new InputStreamReader(connection.getInputStream());
    }
