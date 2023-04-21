    private void getHistoricalData(String symbol, ActionResponse response) throws Exception {
        ArrayList<String> date = new ArrayList<String>();
        ArrayList<BigDecimal> price = new ArrayList<BigDecimal>();
        String tmpurl = getQueryString(symbol);
        URL url = new URL(tmpurl);
        BufferedReader in = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int status = conn.getResponseCode();
            if (status == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer responseBody = new StringBuffer();
                while (true) {
                    String line = in.readLine();
                    if (line == null) break;
                    responseBody.append(line);
                    responseBody.append("\r\n");
                }
                StringTokenizer st = new StringTokenizer(responseBody.toString(), "\r\n");
                int i = 0;
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    i++;
                    if (i == 1) continue;
                    ArrayList<String> split = MiscUtils.splitString(tok.trim(), ",");
                    if (split.size() < 5) continue;
                    String dt = split.get(0).replace('-', '/');
                    date.add(dt);
                    price.add(new BigDecimal(split.get(4)));
                }
                response.addResult("HISTDATES", date);
                response.addResult("HISTPRICE", price);
            } else {
                logger.info("HttpRequest failed. Code=" + status);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
