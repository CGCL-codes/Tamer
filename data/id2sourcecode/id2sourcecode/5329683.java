    private InputStream doPost(String urlString, String content) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        InputStream in = null;
        OutputStream out;
        byte[] buff;
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setDoInput(true);
        HttpURLConnection.setFollowRedirects(false);
        con.setRequestProperty("Cookie", "JSESSIONID=36CBFFF9BB5FC07A0D1594149E571B87; rl-sticky-key=7b368df7d0ec06acf66c7767d7b7c1a0; rl-sticky-key-0c=92bdb2f298601672168bbbae09f5a220");
        con.setRequestProperty("id", urlString.substring(urlString.indexOf("?id=") + 4));
        con.connect();
        out = con.getOutputStream();
        buff = content.getBytes("UTF8");
        out.write(buff);
        out.flush();
        out.close();
        in = con.getInputStream();
        return in;
    }
