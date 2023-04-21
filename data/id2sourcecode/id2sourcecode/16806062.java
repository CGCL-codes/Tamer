    protected double numResultsFromWeb(String term, int step) throws IOException, SQLException {
        double result = 0;
        boolean retry = false;
        if (cache.containsKey(term)) {
            result = cache.get(term);
        } else {
            String source_string = searchEngine.name().toLowerCase() + "-" + corpus.name();
            result = SearchResultCount.findSearchResultCount(term, source_string);
            if (result == -1) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                URL url = null;
                InputStream stream = null;
                try {
                    url = makeQueryURL(term);
                    URLConnection connection = url.openConnection();
                    connection.setRequestProperty("Referer", "http://www.patternsintext.com/");
                    System.setProperty("http.agent", "");
                    stream = connection.getInputStream();
                    InputStreamReader inputReader = new InputStreamReader(stream);
                    BufferedReader bufferedReader = new BufferedReader(inputReader);
                    double count = getCountFromQuery(bufferedReader);
                    cache.put(term, count);
                    newCache.put(term, count);
                    updateCache(CACHE_FILE_NAME);
                    SearchResultCount.insertSearchResultCount(term, source_string, (int) count);
                    result = count;
                } catch (Exception e) {
                    retry = true;
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            Util.log(e.toString(), 3);
                        }
                    }
                }
            }
        }
        if (retry && step < 20) {
            try {
                Thread.sleep(5000 * step);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Util.log("Retrying " + term, 1);
            result = numResultsFromWeb(term, step + 1);
        }
        return result;
    }
