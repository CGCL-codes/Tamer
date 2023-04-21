    private HttpURLConnection openConnection(EndpointReference targetEndpoint) throws MalformedURLException, IOException {
        URL url = new URL(targetEndpoint.getAddress().getValue());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        return connection;
    }
