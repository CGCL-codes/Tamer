    @Override
    public void invalidateAccessToken(LinkedInAccessToken accessToken) {
        if (accessToken == null) {
            throw new IllegalArgumentException("access token cannot be null.");
        }
        try {
            URL url = new URL(LinkedInApiUrls.LINKED_IN_OAUTH_INVALIDATE_TOKEN_URL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            final OAuthConsumer consumer = getOAuthConsumer();
            consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getTokenSecret());
            consumer.sign(request);
            request.connect();
            if (request.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new LinkedInOAuthServiceException(convertStreamToString(request.getErrorStream()));
            }
        } catch (Exception e) {
            throw new LinkedInOAuthServiceException(e);
        }
    }
