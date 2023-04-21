    public static void main(String[] args) {
        try {
            URL url = new URL("http://hostname:80");
            URLConnection conn = url.openConnection();
            conn.connect();
            conn.addRequestProperty("key", "value");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setAllowUserInteraction(true);
            conn.setIfModifiedSince(1000);
            conn.setAllowUserInteraction(true);
            conn.setUseCaches(true);
        } catch (Exception e) {
        }
    }
