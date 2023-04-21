    String get(String urlString, int page) throws IOException {
        URL url = new URL(urlString + "?page=p" + page);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Authorization", "GoogleLogin auth=" + authToken);
        conn.setRequestProperty("User-agent", USER_AGENT);
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line + "\n\r");
        }
        rd.close();
        String result = sb.toString();
        return result;
    }
