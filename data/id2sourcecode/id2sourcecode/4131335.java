    public ActionResponse executeAction(ActionRequest request) throws Exception {
        BufferedReader in = null;
        try {
            String symbol = (String) request.getProperty("SYMBOL");
            if (symbol == null || symbol.length() == 0) symbol = DEFAULT_QUOTE;
            String tmp = URL_QUOTE_DATA.replace("@", symbol);
            ActionResponse resp = new ActionResponse();
            URL url = new URL(tmp);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int status = conn.getResponseCode();
            if (status == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while (true) {
                    String line = in.readLine();
                    if (line == null) break;
                    ArrayList<String> data = parseData(line, ",");
                    getHistoricalData(symbol, resp);
                    resp.addResult("QUOTEDATA", data);
                }
            } else {
                resp.setErrorCode(ActionResponse.GENERAL_ERROR);
                resp.setErrorMessage("HTTP Error [" + status + "]");
            }
            return resp;
        } catch (Exception e) {
            String st = MiscUtils.stackTrace2String(e);
            logger.error(st);
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
