    public void open(String s_url) {
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL(s_url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Cookie", sessionId);
            conn.getInputStream();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
