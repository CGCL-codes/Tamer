    private boolean canAccessGoogleSites(OAuthConsumer consumer, String shortname) throws Exception {
        String target = ScenarioService.GOOGLE_SITES_FEED;
        if (shortname != null) target += shortname + "/";
        URL url = new URL(target);
        HttpURLConnection urlRequest = (HttpURLConnection) url.openConnection();
        consumer.sign(urlRequest);
        urlRequest.connect();
        boolean result = 200 == urlRequest.getResponseCode();
        if (!result) {
            logger.warning("failed request code, msg: " + urlRequest.getResponseCode() + ", " + urlRequest.getResponseMessage());
        }
        return result;
    }
