    @Test
    public void web_apache_httpclient_fetch() throws ClientProtocolException, IOException {
        CharacterQuery query = new CharacterQuery(ServerZone.European, "eldre'thalas", "thapps");
        query.addOption(CharacterQueryOption.Achievements);
        query.addOption(CharacterQueryOption.Companions);
        query.addOption(CharacterQueryOption.HunterPets);
        query.addOption(CharacterQueryOption.Items);
        query.addOption(CharacterQueryOption.Mounts);
        query.addOption(CharacterQueryOption.Reputation);
        query.addOption(CharacterQueryOption.Stats);
        query.addOption(CharacterQueryOption.Talents);
        query.addOption(CharacterQueryOption.Titles);
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(query.getUrl());
        HttpResponse httpPayload = httpclient.execute(httpget);
        if (httpPayload.getEntity() != null && httpPayload.getStatusLine().getStatusCode() == 200) {
            String content = Utils.consumeInputStream(httpPayload.getEntity().getContent());
            System.out.println(content);
            CharacterResponse response = JacksonDeserializer.getInstance().deserializeCharacter(content);
            Utils.dumpCharacter(response);
        } else {
            System.out.println("http status code :" + httpPayload.getStatusLine().getStatusCode());
            String content = Utils.consumeInputStream(httpPayload.getEntity().getContent());
            CharacterResponse response = JacksonDeserializer.getInstance().deserializeCharacter(content);
            Utils.dumpCharacter(response);
        }
    }
