    public static void revokeToken(String protocol, String domain, String token, PrivateKey key) throws IOException, GeneralSecurityException, AuthenticationException {
        String revokeUrl = getRevokeTokenUrl(protocol, domain);
        URL url = new URL(revokeUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        String header = formAuthorizationHeader(token, key, url, "GET");
        httpConn.setRequestProperty("Authorization", header);
        if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new AuthenticationException(httpConn.getResponseCode() + ": " + httpConn.getResponseMessage());
        }
    }
