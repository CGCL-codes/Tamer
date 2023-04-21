    @Override
    public List<SearchResult> search(String query, SortOrder order, int maxResults) throws Exception {
        if (query == null) {
            return null;
        }
        String encodedQuery = "";
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw e;
        }
        final int startAt = 0;
        String url = String.format(RPC_QUERYURL, encodedQuery, String.valueOf(startAt), String.valueOf(maxResults));
        if (order == SortOrder.BySeeders) {
            url += RPC_SORT_SEEDS;
        } else {
            url += RPC_SORT_COMPOSITE;
        }
        HttpParams httpparams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpparams, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpparams, CONNECTION_TIMEOUT);
        DefaultHttpClient httpclient = new DefaultHttpClient(httpparams);
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        InputStream instream = response.getEntity().getContent();
        String result = HttpHelper.ConvertStreamToString(instream);
        result = result.replace(query, encodedQuery);
        JSONObject json = new JSONObject(result);
        instream.close();
        if (json.getInt(RPC_RESULTS) == 0) {
            return new ArrayList<SearchResult>();
        }
        JSONObject list = json.getJSONObject(RPC_ITEMS);
        JSONArray items = list.getJSONArray(RPC_ITEMS_LIST);
        List<SearchResult> results = new ArrayList<SearchResult>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            results.add(new SearchResult(item.getString(RPC_ITEM_TITLE).replaceAll("\\<.*?>", ""), item.getString(RPC_ITEM_URL), item.getString(RPC_ITEM_LINK), item.getString(RPC_ITEM_SIZE), new Date(Date.parse(item.getString(RPC_ITEM_PUBDATE))), item.getInt(RPC_ITEM_SEEDS), item.getInt(RPC_ITEM_LEECHERS)));
        }
        return results;
    }
