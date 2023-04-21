    public static String getAccessToken(String clientId, String clientSecret, String code, String redirectUri) {
        RequestBuilder call = new RequestBuilder(GRAPH_ENDPOINT + "oauth/access_token", HttpMethod.GET);
        call.setTimeout(10 * 1000);
        call.addParam("client_id", clientId);
        call.addParam("client_secret", clientSecret);
        if (code != null || redirectUri != null) {
            call.addParam("code", code);
            call.addParam("redirect_uri", redirectUri);
        } else call.addParam("grant_type", "client_credentials");
        try {
            HttpResponse response = call.execute();
            if (response.getResponseCode() == 200) {
                return URLParser.parseQuery(StringUtils.read(response.getContentStream())).get("access_token");
            } else {
                Later<JsonNode> json = new Now<JsonNode>(new ObjectMapper().readTree(response.getContentStream()));
                new ErrorDetectingWrapper(json).get();
                throw new IllegalStateException("Impossible, this should have been detected as an error: " + json);
            }
        } catch (IOException ex) {
            throw new IOFacebookException(ex);
        }
    }
