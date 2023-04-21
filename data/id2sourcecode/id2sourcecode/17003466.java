    public static Vector<String> sendRequest(String request) throws IOException {
        Vector<String> response = null;
        URL url = new URL(loadProperties().getProperty("url") + request);
        HttpURLConnection connection = null;
        int code = 0;
        int repeat = 10;
        for (; repeat > 0; repeat--) {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "text/xml");
            try {
                code = connection.getResponseCode();
                repeat = 0;
            } catch (java.net.ConnectException e) {
                if (repeat == 1) throw e;
            }
        }
        if (code == 400) {
            response = readResponse(connection.getErrorStream());
        } else {
            response = readResponse(connection.getInputStream());
        }
        connection.disconnect();
        return response;
    }
