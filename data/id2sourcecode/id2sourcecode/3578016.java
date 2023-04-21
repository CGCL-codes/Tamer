    public static String connect(String url_str, String oauth_header, String data) throws IOException {
        URL url = new URL(url_str);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setAllowUserInteraction(true);
        if (oauth_header != null || data != null) {
            conn.setAllowUserInteraction(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            if (oauth_header != null) {
                conn.setRequestProperty("Authorization", oauth_header);
            }
            if (data != null) {
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(data);
                out.flush();
                out.close();
            }
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String out = "";
        String temp;
        while ((temp = reader.readLine()) != null) {
            out += temp;
        }
        return out;
    }
