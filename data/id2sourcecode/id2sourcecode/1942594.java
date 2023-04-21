    private String makeHttpRequest(String token, String url, String item, String httpMethod, int expectedResponseCode) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(httpMethod);
        connection.setRequestProperty("Content-Type", "application/atom+xml");
        connection.setRequestProperty("Authorization", "GoogleLogin auth=" + token);
        connection.setRequestProperty("X-Google-Key", "key=" + DEVELOPER_KEY);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(item.getBytes());
        outputStream.close();
        int responseCode = connection.getResponseCode();
        if (responseCode == expectedResponseCode) {
            return toString(connection.getInputStream());
        } else {
            throw new RuntimeException(toString(connection.getErrorStream()));
        }
    }
