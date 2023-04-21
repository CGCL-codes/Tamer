    protected HttpURLConnection loadDataFromOsm(DataSource dataSource, OsmData data) throws IOException {
        URL url;
        url = new URL(dataSource.getUrl() + data.getUrl());
        System.out.println("url: " + url.toString());
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setReadTimeout(30000);
        return urlConn;
    }
