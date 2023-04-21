    static void run() {
        while (!isMartketOpen()) {
            try {
                System.out.println("Market not open will wait for " + waitTime / 1000 + " seconds");
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PrintWriter quoteWriter = null;
        try {
            String[] headers = new String[] { "Date", "Open", "High", "Low", "Close", "Volume" };
            quoteWriter = new PrintWriter(new FileWriter(stockSymbol + ".csv"));
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        TextQuoteReader quoteReader = new TextQuoteReader(new Security(), null, iciciDirectTemplate);
        LiveQuote prevQuote = null;
        while (!isMarketClosed()) {
            BufferedInputStream reader = null;
            try {
                URLConnection connection = new URL(url).openConnection();
                reader = new BufferedInputStream(connection.getInputStream());
                connection.setReadTimeout(10000);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            int c = 0;
            String source = "";
            try {
                while ((c = reader.read()) != -1) source = source + (char) c;
                try {
                    LiveQuote currQuote = (LiveQuote) quoteReader.read(source);
                    if (currQuote != null) {
                        SimpleQuote quote = new SimpleQuote();
                        if (prevQuote == null) {
                        } else {
                            quote.setDate(currQuote.getLastTradedTime());
                            quote.setOpen(prevQuote.getLastTradedPrice());
                            quote.setClose(currQuote.getLastTradedPrice());
                            quote.setHigh(prevQuote.getLastTradedPrice() > currQuote.getLastTradedPrice() ? prevQuote.getLastTradedPrice() : currQuote.getLastTradedPrice());
                            quote.setLow(prevQuote.getLastTradedPrice() < currQuote.getLastTradedPrice() ? prevQuote.getLastTradedPrice() : currQuote.getLastTradedPrice());
                            quote.setVolume(currQuote.getVolume() - prevQuote.getVolume());
                            quoteWriter.println(quote);
                            System.out.println(quote);
                            quoteWriter.flush();
                        }
                        prevQuote = currQuote;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
