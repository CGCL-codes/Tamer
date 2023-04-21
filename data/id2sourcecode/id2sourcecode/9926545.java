    private String getForex(String code) throws Exception {
        BufferedReader in = null;
        try {
            String tmp = RetrieveForexAction.URL.replace("@", code);
            URL url = new URL(tmp);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int status = conn.getResponseCode();
            if (status == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder value = new StringBuilder();
                while (true) {
                    String line = in.readLine();
                    if (line == null) break;
                    value.append(line);
                }
                return value.toString();
            } else {
                return "0";
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
