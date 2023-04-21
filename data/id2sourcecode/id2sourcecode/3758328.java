    private int httpDELETE(URL url) throws IOException, ProtocolException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestMethod("DELETE");
        connection.connect();
        int response = connection.getResponseCode();
        connection.disconnect();
        return response;
    }
