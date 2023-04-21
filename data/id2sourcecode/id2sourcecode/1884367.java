    private String httpPOST(URL url, String data) throws IOException {
        String result = null;
        URLConnection conn = null;
        if ("http".equals(url.getProtocol())) conn = (HttpURLConnection) url.openConnection();
        if ("https".equals(url.getProtocol())) conn = (HttpsURLConnection) url.openConnection();
        if (conn != null) {
            log.println("Connection opened.");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            log.println("Data sent.");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            if ((line = rd.readLine()) != null) result = line;
            wr.close();
            rd.close();
        }
        return result;
    }
