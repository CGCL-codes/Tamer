    private static void checkURL(String url, String method, int code, String message, String data) throws MalformedURLException, IOException {
        if (data == null) data = "";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        if ("PUT".equals(method)) {
            connection.setRequestProperty("Content-Length", "" + data.getBytes().length);
            connection.setDoOutput(true);
            connection.getOutputStream().write(data.getBytes());
        } else {
            connection.setRequestProperty("Content-Type", "");
        }
        assertEquals(message, code, connection.getResponseCode());
    }
