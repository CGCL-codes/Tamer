    public String getURLWithPost(String url, String query) throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] { new TrustAllManager() };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection httpConn = (HttpsURLConnection) new URL(url).openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.connect();
        OutputStream out = httpConn.getOutputStream();
        out.write(query.getBytes());
        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);
        String inputLine;
        String allLines = "";
        while ((inputLine = in.readLine()) != null) {
            allLines += inputLine;
        }
        out.close();
        in.close();
        return allLines;
    }
