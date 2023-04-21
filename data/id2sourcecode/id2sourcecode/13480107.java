    public static boolean ping(String urlString) {
        boolean ping = false;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) ping = true;
        } catch (Exception e) {
        }
        return ping;
    }
