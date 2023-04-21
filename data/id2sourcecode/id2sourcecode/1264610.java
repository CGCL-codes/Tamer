    protected void request() throws Exception {
        cal.clear();
        contract = getCurrentContract();
        Date begDate = new Date();
        Date endDate = new Date();
        if (getFromTime() <= ANCIENT_TIME) {
            begDate = contract.getBegDate();
            endDate = contract.getEndDate();
        } else {
            cal.setTimeInMillis(getFromTime());
            begDate = cal.getTime();
        }
        cal.setTime(begDate);
        int a = cal.get(Calendar.MONTH);
        int b = cal.get(Calendar.DAY_OF_MONTH);
        int c = cal.get(Calendar.YEAR);
        cal.setTime(endDate);
        int d = cal.get(Calendar.MONTH);
        int e = cal.get(Calendar.DAY_OF_MONTH);
        int f = cal.get(Calendar.YEAR);
        StringBuffer urlStr = new StringBuffer(60);
        urlStr.append("http://www.netfonds.se/quotes/paperhistory.php").append("?paper=");
        urlStr.append(contract.getSymbol());
        URL url = new URL(urlStr.toString());
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setAllowUserInteraction(true);
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(true);
        setInputStream(conn.getInputStream());
    }
