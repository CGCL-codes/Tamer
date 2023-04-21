    public static void main(String[] args) throws Exception {
        OAuthConsumer consumer = new DefaultOAuthConsumer("junk", "junk", SignatureMethod.HMAC_SHA1);
        String scope = "https://www.google.com/fusiontables/api/query";
        OAuthProvider provider = new DefaultOAuthProvider(consumer, "https://www.google.com/accounts/OAuthGetRequestToken?scope=" + URLEncoder.encode(scope, "utf-8"), "https://www.google.com/accounts/OAuthGetAccessToken", "https://www.google.com/accounts/OAuthAuthorizeToken?hd=default");
        logger.debug("Fetching request token...");
        String authUrl = provider.retrieveRequestToken(OAuth.OUT_OF_BAND);
        logger.debug("Request token: " + consumer.getToken());
        logger.debug("Token secret: " + consumer.getTokenSecret());
        logger.debug("Now visit:\n" + authUrl + "\n... and grant this app authorization");
        logger.debug("Enter the verification code and hit ENTER when you're done:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String verificationCode = br.readLine();
        logger.debug("Fetching access token...");
        provider.retrieveAccessToken(verificationCode.trim());
        logger.debug("Access token: " + consumer.getToken());
        logger.debug("Token secret: " + consumer.getTokenSecret());
        URL url = new URL("http://www.blogger.com/feeds/default/blogs");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        consumer.sign(request);
        logger.debug("Sending request...");
        request.connect();
        logger.debug("Response: " + request.getResponseCode() + " " + request.getResponseMessage());
    }
