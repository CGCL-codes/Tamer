    private InputStream doPost(String urlString, String content) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        InputStream in = null;
        OutputStream out;
        byte[] buff;
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.connect();
        out = con.getOutputStream();
        buff = content.getBytes("UTF8");
        out.write(buff);
        out.flush();
        out.close();
        Object[] cookieObject = con.getHeaderFields().get("set-cookie").toArray();
        cookie = (String) cookieObject[0] + " " + (String) cookieObject[1] + " " + (String) cookieObject[2];
        in = con.getInputStream();
        return in;
    }
