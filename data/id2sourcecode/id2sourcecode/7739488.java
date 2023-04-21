    public static String getCardData(String path) throws Exception {
        URL url = new URL("http", "192.168.2.119", 8080, path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        if (conn.getResponseCode() == 200) {
            InputStream inStream = conn.getInputStream();
            byte[] result = readInputStream(inStream);
            String json = new String(result, "UTF-8");
            return json;
        }
        conn.disconnect();
        return null;
    }
