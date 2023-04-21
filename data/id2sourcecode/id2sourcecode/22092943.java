    public Vector getQuotesForSymbol(String symbol) {
        Vector quotes = new Vector();
        if (!connected) return quotes;
        ProgressDialog p = ProgressDialogManager.getProgressDialog();
        p.setNote("Loading quotes for " + symbol);
        try {
            Quote quote;
            URL url = new URL(PROTOCOL, HOST, "/sanford/Members/Research/HistoricalData.asp");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Cookie", cookie);
            OutputStream os = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(os);
            TradingDate startDate = new TradingDate();
            startDate = startDate.previous(365 * 2);
            writer.print("ASXCode=" + symbol + "&" + "StartDate=" + startDate.toString("dd/mm/yy"));
            writer.close();
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                quote = filter.toQuote(line);
                if (quote != null) {
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
