    public Vector getQuotesForDate(TradingDate date, int type) {
        Vector quotes = new Vector();
        if (!connected) return quotes;
        ProgressDialog p = ProgressDialogManager.getProgressDialog();
        p.setNote("Loading quotes");
        try {
            Quote quote;
            URL url = new URL(PROTOCOL, HOST, "/sanford/research/HistoricalData.asp");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Cookie", cookie);
            OutputStream os = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(os);
            writer.print("HistDataDate=" + date.toString("dd/mm/yy"));
            writer.close();
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                quote = filter.toQuote(line);
                if (quote != null && isType(quote, type)) {
                    quotes.add(quote);
                    p.increment();
                }
            }
            reader.close();
        } catch (java.io.IOException io) {
            DesktopManager.showErrorMessage("Error talking to Sanford");
        }
        ProgressDialogManager.closeProgressDialog();
        return quotes;
    }
