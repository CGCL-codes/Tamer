    public void loginOAuth2() throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException, IOException {
        OAuthConsumer consumer = new DefaultOAuthConsumer(getConsumerKey(), getConsumerSecret());
        OAuthProvider provider = new DefaultOAuthProvider(getRequestTokenURL(), getAccessTokenURL(), getAuthorizeURL());
        Log.v(TAG, "Fetching request token ...");
        String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
        Log.v(TAG, "Request token: " + consumer.getToken());
        Log.v(TAG, "Token secret: " + consumer.getTokenSecret());
        Log.v(TAG, "Now visit:\n" + authUrl + "\n... and grant this app authorization");
        Log.v(TAG, "Enter the PIN code and hit ENTER when you're done:");
        String pin = "abc";
        System.out.println("Fetching access token ...");
        provider.retrieveAccessToken(consumer, pin);
        Log.v(TAG, "Access token: " + consumer.getToken());
        Log.v(TAG, "Token secret: " + consumer.getTokenSecret());
        URL url = new URL("http://twitter.com/statuses/mentions.xml");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        consumer.sign(request);
        Log.v(TAG, "Sending request...");
        request.connect();
        Log.v(TAG, "Response: " + request.getResponseCode() + " " + request.getResponseMessage());
    }
