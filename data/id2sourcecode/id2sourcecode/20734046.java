    @Override
    public List<WebSearchResult> search(String term) {
        List<BingResult> results = null;
        try {
            URL url = new URL(generateUrl(term));
            Reader reader = new InputStreamReader(url.openStream(), BingWebSearch.CHARSET);
            BingResponse jsonResults = new Gson().fromJson(reader, BingResponse.class);
            results = jsonResults.getSearchResponse().getWeb().getResults();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<WebSearchResult> bingResults = new ArrayList<WebSearchResult>();
        if (results != null) {
            bingResults.addAll(results);
        }
        return bingResults;
    }
