    public static HttpURLConnection createHttpGetURLConnection(String url) throws IOException, MalformedURLException, ProtocolException {
        URLConnection uc = new URL(url).openConnection();
        HttpURLConnection connection = (HttpURLConnection) uc;
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        return connection;
    }
