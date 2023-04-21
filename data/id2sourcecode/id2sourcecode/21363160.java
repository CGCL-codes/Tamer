    public Candle getLastQuote() {
        Candle candle = new Candle();
        InputStreamReader inStream = null;
        BufferedReader buff = null;
        try {
            URL url = new URL(provider + symbol.getSymbolCode() + tags);
            URLConnection urlConn = url.openConnection();
            inStream = new InputStreamReader(urlConn.getInputStream());
            buff = new BufferedReader(inStream);
            String csvString = buff.readLine();
            String ticker = "n/a";
            String close = "n/a";
            String date = "n/a";
            String time = "n/a";
            String open = "n/a";
            String high = "n/a";
            String low = "n/a";
            String vol = "n/a";
            try {
                StringTokenizer tokenizer = new StringTokenizer(csvString, ",");
                ticker = tokenizer.nextToken();
                close = tokenizer.nextToken();
                date = tokenizer.nextToken();
                time = tokenizer.nextToken();
                open = tokenizer.nextToken();
                high = tokenizer.nextToken();
                low = tokenizer.nextToken();
                vol = tokenizer.nextToken();
                StringTokenizer dateToken = new StringTokenizer(date.replaceAll("\"", "").trim(), "/");
                String mon = dateToken.nextToken();
                String day = dateToken.nextToken();
                String yyyy = dateToken.nextToken();
                if (mon.length() < 2) mon = "0" + mon;
                if (day.length() < 2) day = "0" + day;
                date = yyyy + mon + day;
            } catch (Exception e) {
            }
            time = time.replace(":", "").replace("am", "").replace("pm", "").trim();
            candle = setCandle(open, close, date, time, high, low, vol, null);
            if (candle.validate(this.timeFrame.isIntraday()) < 0) System.out.println("Quote is wrong!");
        } catch (MalformedURLException e) {
            System.out.println("Please check the spelling of the URL:" + e.toString());
        } catch (IOException e1) {
            System.out.println("Can't read from the Internet: " + e1.toString());
        } finally {
            try {
                inStream.close();
                buff.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return candle;
    }
