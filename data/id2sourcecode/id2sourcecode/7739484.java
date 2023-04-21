    public static String getWebData(String path) throws Exception {
        Log.i("PATH", path);
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        Log.i("CODE", conn.getResponseCode() + "");
        if (conn.getResponseCode() == 200) {
            InputStream inStream = conn.getInputStream();
            byte[] result = readInputStream(inStream);
            String json = new String(result, "UTF-8");
            return json;
        }
        return null;
    }
