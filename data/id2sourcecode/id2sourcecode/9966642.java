    private String getAccessToken(String params) throws IOException {
        StringBuilder constantParams = new StringBuilder().append("client_id=").append(Constants.APP_ID).append("&client_secret=").append(Constants.SECRET_KEY);
        params = constantParams.append(params).toString();
        URL url = new URL(OAUTH_URL);
        URLConnection conn = url.openConnection();
        try {
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            conn.getOutputStream().write(params.toString().getBytes());
            conn.getOutputStream().flush();
        } finally {
            conn.getOutputStream().close();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            String inputLine = in.readLine();
            if (inputLine == null || !inputLine.contains("=")) {
                return null;
            }
            String accessToken = inputLine.split("=")[1];
            return accessToken;
        } finally {
            in.close();
        }
    }
