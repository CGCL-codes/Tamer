    public static String openUrl(String url, String method, Bundle params) {
        if (method.equals("GET")) {
            url = url + "?" + encodeUrl(params);
        }
        String response = "";
        try {
            Log.d(LOG_TAG, method + " URL: " + url);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent") + " RenrenAndroidSDK");
            if (!method.equals("GET")) {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.getOutputStream().write(encodeUrl(params).getBytes("UTF-8"));
            }
            response = read(conn.getInputStream());
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return response;
    }
