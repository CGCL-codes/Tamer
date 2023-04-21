    public boolean authenticate(String user, String pass) throws IOException {
        URL auth_url = new URL(AUTH_URL);
        HttpURLConnection urlConn = (HttpURLConnection) auth_url.openConnection();
        urlConn.setInstanceFollowRedirects(false);
        urlConn.setDoOutput(true);
        urlConn.setRequestMethod("POST");
        urlConn.connect();
        String credstr = "credential_0=" + URLEncoder.encode(user) + "&credential_1=" + URLEncoder.encode(pass) + "&noexpire=1";
        OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream());
        out.write(credstr);
        out.flush();
        out.close();
        String cookie_header = urlConn.getHeaderField("Set-Cookie");
        if (null == cookie_header) return false;
        StringTokenizer st = new StringTokenizer(cookie_header, ";");
        cookie = st.nextToken();
        return true;
    }
