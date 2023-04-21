    public static HttpURLConnection httpDoGet(String strAbsUrlWithParams, HashMap hashHeaders, StringBuffer bufOutput) throws MalformedURLException, IOException {
        URL url = new URL(strAbsUrlWithParams);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        httpDoGet(conn, hashHeaders, bufOutput);
        return conn;
    }
