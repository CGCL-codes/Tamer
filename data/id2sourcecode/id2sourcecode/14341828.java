    public static Map getTokenInfo(String protocol, String domain, String token, PrivateKey key) throws IOException, GeneralSecurityException, AuthenticationException {
        String tokenInfoUrl = getTokenInfoUrl(protocol, domain);
        URL url = new URL(tokenInfoUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        String header = formAuthorizationHeader(token, key, url, "GET");
        httpConn.setRequestProperty("Authorization", header);
        if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new AuthenticationException(httpConn.getResponseCode() + ": " + httpConn.getResponseMessage());
        }
        String body = IOUtils.toString(httpConn.getInputStream());
        return StringUtils.string2Map(body.trim(), "\n", "=", true);
    }
