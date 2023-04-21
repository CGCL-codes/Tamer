    public static String get(URL url) {
        StringBuilder ret = new StringBuilder();
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            int i = 0;
            while (i != -1) {
                i = in.read();
                if (i != -1) {
                    char c = (char) i;
                    ret.append(c);
                }
            }
        } catch (IOException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
            ret.append("Error connecting to the URL");
        }
        return ret.toString();
    }
