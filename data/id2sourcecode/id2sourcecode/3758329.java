    private int httpGET(URL url) throws IOException, ProtocolException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestMethod("GET");
        connection.connect();
        int response = connection.getResponseCode();
        connection.disconnect();
        return response;
    }
