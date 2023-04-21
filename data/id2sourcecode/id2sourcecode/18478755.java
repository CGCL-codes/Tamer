    private URLConnection createConnection(URL url) throws IOException {
        URLConnectionFactory factory = getURLConnectionFactory();
        return (factory == null) ? url.openConnection() : factory.createURLConnection(url);
    }
