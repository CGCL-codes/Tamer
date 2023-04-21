    protected void request() throws Exception {
        cal.clear();
        StringBuilder urlStr = new StringBuilder(90);
        urlStr.append("http://quote.yahoo.com/d/quotes.csv").append("?s=");
        Collection<TickerContract> contracts = getSubscribedContracts();
        if (contracts.size() == 0) {
            setInputStream(null);
            setLoadedTime(getFromTime());
            return;
        }
        for (TickerContract contract : contracts) {
            urlStr.append(contract.getSymbol()).append("+");
        }
        urlStr = urlStr.deleteCharAt(urlStr.length() - 1);
        urlStr.append("&d=t&f=sl1d1t1c1ohgvbap");
        String urlStrForName = urlStr.append("&d=t&f=snx").toString();
        URL url = new URL(urlStr.toString());
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setAllowUserInteraction(true);
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(true);
        setInputStream(conn.getInputStream());
    }
