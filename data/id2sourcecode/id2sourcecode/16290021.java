    HttpURLConnection createHttpConnection() throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(urlWithRequestParams());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }
