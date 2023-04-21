    private HttpURLConnection openConnectionFollowRedirects(String urlStr, String authSubToken) throws MalformedURLException, GeneralSecurityException, IOException {
        boolean redirectsDone = false;
        HttpURLConnection connection = null;
        while (!redirectsDone) {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String authHeader = null;
            authHeader = AuthSubUtil.formAuthorizationHeader(authSubToken, Utility.getPrivateKey(), url, "GET");
            connection.setRequestProperty("Authorization", authHeader);
            connection.setInstanceFollowRedirects(false);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                urlStr = connection.getHeaderField("Location");
                if (urlStr == null) {
                    redirectsDone = true;
                }
            } else {
                redirectsDone = true;
            }
        }
        return connection;
    }
