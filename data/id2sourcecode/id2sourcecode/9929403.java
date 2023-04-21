    public String query_remote(String url, String r_url, String r_username, String r_password) throws IOException {
        URL remURL = new URL(r_url + "?w3c_annotates=" + URLEncoder.encode(url, "UTF-8").toString());
        System.out.println("Remote URL to query " + remURL);
        URLConnection url_con = remURL.openConnection();
        if (useAuthorization()) {
            url_con.setRequestProperty("Authorization", "Basic " + getBasicAuthorizationString());
        }
        url_con.setRequestProperty("Accept", "application/xml");
        InputStream content = url_con.getInputStream();
        BufferedReader inRead = new BufferedReader(new InputStreamReader(content, "UTF-8"));
        String out = "";
        while (inRead.ready()) out = out + inRead.readLine() + "\n";
        return out;
    }
