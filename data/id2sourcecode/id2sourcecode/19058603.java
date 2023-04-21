    @SuppressWarnings("unchecked")
    public boolean retrieveTrustedAccessToken(String user_name, String user_password) throws OAuthCommunicationException, OAuthMessageSignerException, IOException, ParseException, OAuthExpectationFailedException, DropboxException {
        assert config != null : "Config was not set.";
        assert user_name != null : "Must set a user name to create a token for.";
        assert user_password != null : "Must set a user_password to create a token for.";
        Object[] params = { "email", user_name, "password", user_password };
        OAuthConsumer cons = new CommonsHttpOAuthConsumer(consumer_key, consumer_secret);
        String target = RESTUtility.buildFullURL(RESTUtility.secureProtocol(), api_host, port, RESTUtility.buildURL("/token", API_VERSION, params));
        HttpGet req = new HttpGet(target);
        cons.sign(req);
        HttpClient client = RESTUtility.getClient(target);
        try {
            HttpResponse response = client.execute(req);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 401 || responseCode == 404) {
                return false;
            } else if (responseCode != 200) {
                throw new DropboxException(response.getStatusLine().toString());
            }
            Map creds = (Map) RESTUtility.parseAsJSON(response);
            consumer.setTokenWithSecret((String) creds.get("token"), (String) creds.get("secret"));
        } catch (UnknownHostException uhe) {
            throw new DropboxException(uhe);
        }
        return true;
    }
