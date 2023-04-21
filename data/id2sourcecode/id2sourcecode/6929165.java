    public URLConnection configureURLConnectionWithAuthentication(String uri, String userName, String passWord) throws IOException {
        String input = userName + ":" + passWord;
        URL url = new URL(uri);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(settings.getNetConnectSlowTimeout());
        con.setReadTimeout(settings.getNetReadSlowTimeout());
        String encoding = base64Encode(input);
        con.setRequestProperty("Authorization", "Basic " + encoding);
        return con;
    }
