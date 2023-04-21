    public String getScreenShot(String ticker) {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        ticker = ticker.toLowerCase();
        String yahooFinance = null;
        String yahooAddress = ("http://finance.yahoo.com/q?s=" + ticker);
        try {
            url = new URL(yahooAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((yahooFinance = in.readLine()) != null) {
                    sb.append(yahooFinance);
                }
            } else {
                System.out.println("Response code: " + responseCode + " ----  Error reading url: " + yahooAddress);
            }
        } catch (IOException e) {
            System.err.println("IOException attempting to read url " + ticker);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ignore) {
                }
            }
        }
        String screenShot = sb.toString();
        return screenShot;
    }
