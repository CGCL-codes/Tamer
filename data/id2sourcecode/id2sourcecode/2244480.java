    private synchronized void getStockInfo(String urlStr) {
        try {
            textView.setText(null);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlStr);
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "GBK"));
            String line = "";
            String allLines = "";
            while ((line = rd.readLine()) != null) {
                textView.append(line);
                allLines = allLines + line;
            }
            Map<String, Stock> stockInfos = StringParser.praseStockString(allLines);
            String stockCode;
            TextView stockCodeView;
            TextView currentPriceView;
            TextView stockHighWarnPriceView;
            TextView stockLowWarnPriceView;
            Stock stock;
            double currentPrice;
            double highWarnPrice;
            double lowWarnPrice;
            for (int i = 0; i < this.listView.getChildCount(); i++) {
                stockCodeView = (TextView) this.listView.getChildAt(i).findViewById(R.id.StockCodeView);
                currentPriceView = (TextView) this.listView.getChildAt(i).findViewById(R.id.StockCurrentPriceView);
                stockHighWarnPriceView = (TextView) this.listView.getChildAt(i).findViewById(R.id.StockHighWarnPriceView);
                stockLowWarnPriceView = (TextView) this.listView.getChildAt(i).findViewById(R.id.StockLowWarnPriceView);
                highWarnPrice = Double.parseDouble(stockHighWarnPriceView.getText().toString().trim());
                lowWarnPrice = Double.parseDouble(stockLowWarnPriceView.getText().toString().trim());
                stockCode = stockCodeView.getText().toString().trim();
                stock = stockInfos.get(stockCode);
                currentPrice = stock.getCurrentPrice();
                currentPriceView.setText(String.valueOf(currentPrice));
                if (currentPrice >= highWarnPrice) {
                    System.out.println("UP");
                }
                if (currentPrice <= lowWarnPrice) {
                    System.out.println("DOWN");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            textView.setText(ex.getMessage());
        }
    }
