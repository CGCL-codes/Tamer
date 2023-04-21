            @Override
            public void run() {
                Set<EquityPricingInformation> setOfEquityPricingInformation = new HashSet<EquityPricingInformation>();
                Log.i(YahooEquityPricingInformationFeed.class.getName(), "Yahoo we are");
                try {
                    Set<String> allTickers = getTickers();
                    Log.i(TAG, "Observed tickers:" + allTickers);
                    if (allTickers.isEmpty()) {
                        Log.i(TAG, "No observed tickers");
                        return;
                    }
                    StringBuffer tickerList = new StringBuffer();
                    for (String ticker : allTickers) {
                        if (tickerList.length() != 0) {
                            tickerList.append(",");
                        }
                        tickerList.append(ticker);
                    }
                    String url = "http://download.finance.yahoo.com/d/quotes.csv?s=" + tickerList.toString() + "&f=sb2b3jkm6";
                    Log.i(TAG, url);
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        String ticker = parts[0].replace("\"", "");
                        Log.i(TAG, "TICKER:" + ticker);
                        Log.i(TAG, line);
                        EquityPricingInformation information = new EquityPricingInformation(ticker, ticker, new BigDecimal(parts[1]), null, new Date(), new BigDecimal(parts[2]), null, new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
                        Log.i(TAG, "Object not NULL?" + information);
                        setOfEquityPricingInformation.add(information);
                    }
                    Log.i(TAG, "Notfiying everyone");
                    notifyObservers(setOfEquityPricingInformation);
                } catch (Exception e) {
                    Log.e(TAG, "Unable to get stock quotes", e);
                }
            }
