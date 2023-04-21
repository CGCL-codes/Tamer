    public static String getHtmlSource(String url) throws Exception {
        String sCurrentLine;
        String sTotalString;
        sCurrentLine = "";
        sTotalString = "";
        java.io.InputStream l_urlStream;
        java.net.URL l_url = new java.net.URL(url);
        java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
        if (System.getProperty("java.runtime.version").startsWith("1.4")) {
            System.setProperty("sun.net.client.defaultConnectTimeout", "3000");
            System.setProperty("sun.net.client.defaultReadTimeout", "3000");
        } else {
            l_connection.setConnectTimeout(3000);
            l_connection.setReadTimeout(3000);
        }
        l_connection.connect();
        l_urlStream = l_connection.getInputStream();
        java.io.BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));
        while ((sCurrentLine = l_reader.readLine()) != null) {
            sTotalString += sCurrentLine;
        }
        return sTotalString;
    }
