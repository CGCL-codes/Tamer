    public void fetchDataset(String symbol, Interval interval) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(interval.startTime());
        Calendar endCalendar = Calendar.getInstance();
        String url = NbBundle.getMessage(YahooDataFeed.class, "YahooDataFeed.historicalPrices.url", new String[] { symbol, Integer.toString(startCalendar.get(Calendar.MONTH)), Integer.toString(startCalendar.get(Calendar.DAY_OF_MONTH)), Integer.toString(startCalendar.get(Calendar.YEAR)), Integer.toString(endCalendar.get(Calendar.MONTH)), Integer.toString(endCalendar.get(Calendar.DAY_OF_MONTH)), Integer.toString(endCalendar.get(Calendar.YEAR)), interval.getTimeParam() });
        HttpContext context = new BasicHttpContext();
        HttpGet method = new HttpGet(url);
        try {
            HttpResponse response = ProxyManager.httpClient.execute(method, context);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String get = EntityUtils.toString(entity);
                String[] lines = get.split("\n");
                if (lines.length > 0) {
                    ArrayList<DataItem> items = new ArrayList<DataItem>();
                    for (int i = 1; i < lines.length; i++) {
                        String[] values = lines[i].split(",");
                        DataItem item = new DataItem(dateFormat.parse(values[0]).getTime(), Float.parseFloat(values[1]), Float.parseFloat(values[2]), Float.parseFloat(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                        items.add(item);
                        item = null;
                    }
                    Collections.sort(items);
                    addDataset(symbol, interval, new Dataset(items.toArray(new DataItem[items.size()])));
                    items = null;
                }
                get = null;
                lines = null;
                EntityUtils.consume(entity);
            }
        } catch (Exception ex) {
            return;
        } finally {
            method.abort();
        }
    }
