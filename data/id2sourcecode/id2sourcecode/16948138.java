    public String send(String json) throws IOException {
        String urlStr = "http://" + (isUsingTestServer() ? "test" : "www") + ".freebase.com/api/service/" + getAPIService();
        URL url = new URL(urlStr);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Cookie", COOKIE + "=" + "\"" + getCookie() + "\"");
        connection.setRequestProperty("X-Metaweb-Request", "HelloMetaweb");
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write("queries=" + URLEncoder.encode(json, "UTF-8"));
        out.close();
        InputStream in = connection.getInputStream();
        String result = IOUtils.getReaderContent(new InputStreamReader(in));
        in.close();
        return result;
    }
