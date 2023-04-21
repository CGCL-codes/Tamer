    private static String downloadViaHttp(String uri, String contentTypes) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", contentTypes);
        connection.setRequestProperty("Accept-Charset", "utf-8,*");
        connection.setRequestProperty("User-Agent", "ZXing (Android)");
        try {
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Bad HTTP response: " + connection.getResponseCode());
            }
            return consume(connection);
        } finally {
            connection.disconnect();
        }
    }
