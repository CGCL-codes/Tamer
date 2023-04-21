    private static HttpURLConnection getConnection(final String strURL) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(strURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String encoding = new String(Base64.encodeBase64((MendozaTestConstants.merchantId + ":" + MendozaTestConstants.merchantKey).getBytes()));
            connection.setRequestProperty("Authorization", "Basic " + encoding);
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server", e);
        }
        if (connection == null) {
            throw new RuntimeException("Attempted to return a null connection");
        }
        return connection;
    }
