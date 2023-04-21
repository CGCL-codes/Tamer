    public String getAuthTokenWithGaeHttpApi() throws AuthTokenException {
        try {
            UriBuilder uriBuilder = new UriBuilder(Uri.parse(CLIENT_LOGIN_URL));
            Map<String, String> paramsMap = getClientLoginParams();
            for (Entry<String, String> entry : paramsMap.entrySet()) {
                uriBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            Uri uri = uriBuilder.toUri();
            URL url = uri.toJavaUri().toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(HTTP_CLIENT_SOCKET_TIMEOUT_IN_MS);
            HTTPRequest httpRequest = new HTTPRequest(url);
            URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
            HTTPResponse httpResponse = urlFetchService.fetch(httpRequest);
            int statusCode = httpResponse.getResponseCode();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(httpResponse.getContent());
            Properties responseProperties = generatePropertiesFromResponse(byteArrayInputStream);
            String responseBody = new String(httpResponse.getContent());
            return processClientLoginResponse(statusCode, responseProperties, responseBody);
        } catch (IOException e) {
            throw new AuthTokenException(null, null, null, null, e);
        }
    }
