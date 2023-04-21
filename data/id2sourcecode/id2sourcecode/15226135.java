    public QuoteFetcherThread(String stockName, String url, RegexQuoteTemplate quoteTemplate, PrintWriter writer) {
        this.stockName = stockName;
        this.url = url;
        this.quoteTemplate = quoteTemplate;
        this.writer = writer;
    }
