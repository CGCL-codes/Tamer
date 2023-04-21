    public HttpURLConnection getConnection(String uri) throws IOException {
        HttpURLConnection connection = null;
        uri = uri.replaceAll("^(https://|http://)", "");
        URL url = new URL("http://" + uri);
        connection = (HttpURLConnection) url.openConnection();
        return connection;
    }
