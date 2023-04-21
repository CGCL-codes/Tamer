    public String fetchStockCompanyName(Stock stock) {
        String companyName = "";
        String symbol = StockUtil.getStock(stock);
        if (isStockCached(symbol)) {
            return getStockFromCache(symbol);
        }
        String url = NbBundle.getMessage(YahooDataFeed.class, "YahooDataFeed.stockInfo.url", new String[] { symbol });
        HttpContext context = new BasicHttpContext();
        HttpGet method = new HttpGet(url);
        try {
            HttpResponse response = ProxyManager.httpClient.execute(method, context);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                companyName = StringUtil.stringBetween(EntityUtils.toString(entity), "<td width=\"270\" class=\"yfnc_modtitlew1\"><b>", "</b><br>");
                cacheStock(symbol, companyName);
                EntityUtils.consume(entity);
            }
        } catch (Exception ex) {
            companyName = "";
        } finally {
            method.abort();
        }
        return companyName;
    }
