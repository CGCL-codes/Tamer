    private static InputStream getStreamOfConnection(String requestUrl) throws MalformedURLException, IOException {
        URL url = new URL(requestUrl.toString());
        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }
