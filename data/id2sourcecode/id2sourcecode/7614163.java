    private static String lookupGenusLsid(String id) throws Exception {
        HttpClient client = new DefaultHttpClient();
        URL wsUrl = new URL("http://bie.ala.org.au/search.json?fq=rank:genus&q=" + id);
        URI uri = new URI(wsUrl.getProtocol(), wsUrl.getAuthority(), wsUrl.getPath(), wsUrl.getQuery(), wsUrl.getRef());
        HttpGet get = new HttpGet(uri.toURL().toString());
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IllegalStateException("Fetching of species or family LSID failed");
        }
        HttpEntity entity = response.getEntity();
        String responseContent = IOUtils.toString(entity.getContent());
        JSONObject jsonObj = (JSONObject) new JSONParser().parse(responseContent);
        String genusLsid = null;
        if (jsonObj.containsKey("searchResults")) {
            JSONObject searchResultsObj = (JSONObject) jsonObj.get("searchResults");
            if (searchResultsObj.containsKey("results")) {
                JSONArray resultsArray = (JSONArray) searchResultsObj.get("results");
                if (resultsArray.size() > 0) {
                    JSONObject firstResult = (JSONObject) resultsArray.get(0);
                    if (firstResult.containsKey("guid")) {
                        genusLsid = (String) firstResult.get("guid");
                    }
                }
            }
        }
        return genusLsid;
    }
