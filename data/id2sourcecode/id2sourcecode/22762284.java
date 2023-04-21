    public static Object openConnection(String connection_url, String content, String method, String contentType) throws IOException {
        URL url = new URL(connection_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        if (method != null) {
            connection.setRequestMethod(method);
        }
        if (contentType != null) {
            connection.setRequestProperty("Content-Type", contentType);
        }
        if (content != null) {
            connection.setRequestProperty("Content-Length", "" + content.length());
            OutputStream out = null;
            try {
                out = connection.getOutputStream();
                out.write(content.getBytes());
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        }
        return connection.getResponseCode() + ":" + connection.getResponseMessage();
    }
