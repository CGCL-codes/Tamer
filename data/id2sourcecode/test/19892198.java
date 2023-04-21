    private static HttpURLConnection getCon(String url) throws MalformedURLException, IOException {
        HttpURLConnection r = (HttpURLConnection) (new URL(host + url).openConnection());
        r.addRequestProperty("Cookie", "dev_appserver_login=get_html@localhost.devel:false:18580476422013912411");
        r.setRequestMethod("GET");
        r.setReadTimeout(15000);
        r.connect();
        return r;
    }
