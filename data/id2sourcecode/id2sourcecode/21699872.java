    @Test
    public void web_apache_httpclient_fetch() throws ClientProtocolException, IOException {
        RealmStatusQuery query = new RealmStatusQuery(ServerZone.European);
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(query.getUrl());
        HttpResponse httpPayload = httpclient.execute(httpget);
        if (httpPayload.getEntity() != null && httpPayload.getStatusLine().getStatusCode() == 200) {
            String content = Utils.consumeInputStream(httpPayload.getEntity().getContent());
            RealmStatusResponse response = JacksonDeserializer.getInstance().deserializeRealmStatus(content);
            Utils.dumpRealmStatus(response);
        }
    }
