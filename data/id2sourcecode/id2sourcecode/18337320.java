    @Test
    public void web_apache_httpclient_fetch() throws ClientProtocolException, IOException {
        GuildQuery query = new GuildQuery(ServerZone.European, "EldreThalas", "Hors%20dEux");
        query.addOption(GuildQueryOption.Achievements);
        query.addOption(GuildQueryOption.Members);
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(query.getUrl());
        HttpResponse httpPayload = httpclient.execute(httpget);
        if (httpPayload.getEntity() != null && httpPayload.getStatusLine().getStatusCode() == 200) {
            String content = Utils.consumeInputStream(httpPayload.getEntity().getContent());
            System.out.println(query.getUrl());
            System.out.println(content);
            GuildResponse response = JacksonDeserializer.getInstance().getMapper().readValue(content, GuildResponse.class);
        }
    }
