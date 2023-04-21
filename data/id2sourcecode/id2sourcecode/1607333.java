    public static double toExchange(String sourceCode, String targetCode, double count) {
        final String DEBUG_TAG = "ExchangeManager";
        String httpUrl = "http://download.finance.yahoo.com/d/quotes.html?s=" + sourceCode + targetCode + "=X&f=l1";
        String resultData = "";
        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
            Log.e(DEBUG_TAG, "MalformedURLException");
        }
        if (url != null) {
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                String inputLine = null;
                while (((inputLine = buffer.readLine()) != null)) {
                    resultData += inputLine + "\n";
                }
                Log.e(DEBUG_TAG, httpUrl);
                in.close();
                urlConn.disconnect();
                if (resultData != null) {
                    Log.e(DEBUG_TAG, resultData);
                    count *= (Double.parseDouble(resultData));
                } else {
                    Log.e(DEBUG_TAG, "��ȡ������ΪNULL");
                }
            } catch (IOException e) {
                Log.e(DEBUG_TAG, "IOException");
            }
        } else {
            Log.e(DEBUG_TAG, "Url NULL");
        }
        return count;
    }
