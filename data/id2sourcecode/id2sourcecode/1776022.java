    private HttpURLConnection getConnection(String method, OpenSocialUrl url, String contentType) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url.toString()).openConnection();
        if (contentType != null && !contentType.equals("")) {
            connection.setRequestProperty(HttpMessage.CONTENT_TYPE, contentType);
        }
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.connect();
        return connection;
    }
