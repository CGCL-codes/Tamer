    public void invokeGU(String testEvent, String url) throws UnsupportedEncodingException, IOException, ClientProtocolException {
        HttpContext localContext = new BasicHttpContext();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("ORIGIN", "TEST");
        String eventAsString = testEvent;
        StringEntity entity = new StringEntity(eventAsString);
        httppost.setEntity(entity);
        log("Sending message " + eventAsString);
        long time = System.currentTimeMillis();
        HttpResponse response = httpclient.execute(httppost, localContext);
        log("time elapsed " + (System.currentTimeMillis() - time));
        log("getReasonPhrase " + response.getStatusLine().getReasonPhrase());
        httpclient.getConnectionManager().closeExpiredConnections();
    }
