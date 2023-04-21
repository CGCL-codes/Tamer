    public synchronized Map<String, Stock> getStockInfo(String urlStr) {
        Map<String, Stock> stockInfos = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlStr);
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "GBK"));
            String line = "";
            String allLines = "";
            while ((line = rd.readLine()) != null) {
                allLines = allLines + line;
            }
            stockInfos = StringParser.praseStockString(allLines);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stockInfos;
    }
