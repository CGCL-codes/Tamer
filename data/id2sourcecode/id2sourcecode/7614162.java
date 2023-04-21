    private static String lookupSpeciesOrFamilyLsid(String id) throws Exception {
        HttpClient client = new DefaultHttpClient();
        URL wsUrl = new URL("http://bie.ala.org.au/ws/guid/" + id);
        URI uri = new URI(wsUrl.getProtocol(), wsUrl.getAuthority(), wsUrl.getPath(), wsUrl.getQuery(), wsUrl.getRef());
        HttpGet get = new HttpGet(uri.toURL().toString());
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IllegalStateException("Fetching of species or family LSID failed");
        }
        HttpEntity entity = response.getEntity();
        String responseContent = IOUtils.toString(entity.getContent());
        JSONArray jsonArr = (JSONArray) new JSONParser().parse(responseContent);
        String lsid = null;
        if (jsonArr.size() > 0) {
            JSONObject jsonObj = (JSONObject) jsonArr.get(0);
            lsid = (String) jsonObj.get("identifier");
        }
        return lsid;
    }
