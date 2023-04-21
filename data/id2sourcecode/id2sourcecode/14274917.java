    public static String getContent(String urlName) throws Exception {
        URL url = new URL(urlName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String response = new String();
        response = readFromConnection(connection);
        connection.disconnect();
        return response;
    }
