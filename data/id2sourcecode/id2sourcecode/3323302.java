    private HttpURLConnection getConnection(String address) throws MalformedURLException, IOException {
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append("http://");
        urlBuffer.append(getServer());
        urlBuffer.append(address);
        URL url = new URL(urlBuffer.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);
        String auth = getUsername() + ":" + getPassword();
        conn.setRequestProperty("Authorization", "Basic " + B64Encode(auth.getBytes()));
        if (showAuth) {
            System.out.println("AUTH: " + auth);
        }
        return conn;
    }
