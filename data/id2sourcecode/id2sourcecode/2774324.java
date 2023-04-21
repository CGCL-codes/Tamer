    private void populatePortfolioEntry(ActionResponse resp, PortfolioEntry e) throws Exception {
        String tmp = URL_QUOTE_DATA.replace("@", e.getSymbol());
        URL url = new URL(tmp);
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
                }
                ArrayList<String> data = parseData(responseBody.toString(), ",");
                if (data.size() == 2) {
                    e.setName(MiscUtils.trimChars(data.get(0), '"'));
                    String val = data.get(1);
                    val = MiscUtils.trimChars(val.trim(), '\r');
                    val = MiscUtils.trimChars(val.trim(), '\n');
                    BigDecimal d = new BigDecimal(val);
                    e.setPricePerShare(d);
                } else {
                    resp.setErrorCode(ActionResponse.GENERAL_ERROR);
                    resp.setErrorMessage("Error retrieving data");
                }
            } else {
                resp.setErrorCode(ActionResponse.GENERAL_ERROR);
                resp.setErrorMessage("Error retrieving data Http code: " + status);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
