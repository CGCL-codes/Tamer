    public static HttpURLConnection httpDoPost(String strAbsUrl, HashMap hashParams, HashMap hashHeaders, StringBuffer bufOutput) throws MalformedURLException, IOException {
        URL url = new URL(strAbsUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        httpDoPost(conn, hashParams, hashHeaders, bufOutput);
        return conn;
    }
