    public String getResponse(TwitterRequest twitterRequest, OAuthConsumer consumer) throws Exception {
        URL url = new URL(twitterRequest.url() + twitterRequest.paramString());
        HttpURLConnection req = (HttpURLConnection) url.openConnection();
        consumer.sign(req);
        req.connect();
        return Util.mkString(req.getInputStream());
    }
