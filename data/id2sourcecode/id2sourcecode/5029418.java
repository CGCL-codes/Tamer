    private long deletePLace(int i) throws IOException {
        long startTime = System.currentTimeMillis();
        URL url = new URL(URL_REST);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(false);
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.connect();
        conn.getInputStream();
        long time = System.currentTimeMillis() - startTime;
        return time;
    }
