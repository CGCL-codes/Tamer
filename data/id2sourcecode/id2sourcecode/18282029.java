    public int getResponseCode0(String file) throws Exception {
        HttpURLConnection urlconn = null;
        try {
            String target = file + "?jsp_precompile=true";
            URL url = new URL("http", host, port, target);
            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.connect();
            int respCode = urlconn.getResponseCode();
            return respCode;
        } finally {
            try {
                if (urlconn != null) urlconn.disconnect();
            } catch (Exception exc2) {
            }
        }
    }
