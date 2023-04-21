    private HttpURLConnection requestReservation() {
        try {
            connection = HttpTransportManager.openConnection(url, basicAuthUsername, basicAuthPassword);
            connection.setUseCaches(false);
            connection.setConnectTimeout(httpTimeout);
            connection.setReadTimeout(httpTimeout);
            connection.setRequestMethod("HEAD");
            analyzeResponseCode(connection.getResponseCode());
        } catch (IOException ex) {
            throw new IoException(ex);
        }
        return connection;
    }
