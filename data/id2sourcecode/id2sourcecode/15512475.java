    public String postRequest(TwitterRequest twitterRequest, OAuthConsumer consumer) throws Exception {
        URL url = new URL(twitterRequest.url() + twitterRequest.paramString());
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("POST");
        consumer.sign(request);
        request.connect();
        return Util.mkString(request.getInputStream());
    }
