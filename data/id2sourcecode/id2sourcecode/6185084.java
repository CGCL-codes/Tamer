    protected void request() throws Exception {
        calendar.clear();
        contract = getCurrentContract();
        Date begDate = new Date();
        Date endDate = new Date();
        if (getFromTime() <= ANCIENT_TIME) {
            begDate = contract.getBegDate();
            endDate = contract.getEndDate();
        } else {
            calendar.setTimeInMillis(getFromTime());
            begDate = calendar.getTime();
        }
        calendar.setTime(begDate);
        int a = calendar.get(Calendar.MONTH);
        int b = calendar.get(Calendar.DAY_OF_MONTH);
        int c = calendar.get(Calendar.YEAR);
        calendar.setTime(endDate);
        int d = calendar.get(Calendar.MONTH);
        int e = calendar.get(Calendar.DAY_OF_MONTH);
        int f = calendar.get(Calendar.YEAR);
        StringBuilder urlStr = new StringBuilder(30);
        urlStr.append("http://table.finance.yahoo.com/table.csv").append("?s=");
        urlStr.append(contract.getSymbol());
        urlStr.append("&a=" + a + "&b=" + b + "&c=" + c + "&d=" + d + "&e=" + e + "&f=" + f);
        urlStr.append("&g=d&ignore=.csv");
        URL url = new URL(urlStr.toString());
        System.out.println(url);
        if (url != null) {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(true);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            setInputStream(conn.getInputStream());
        }
    }
