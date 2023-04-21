    public String get(String urlStr) throws MalformedURLException, IOException {
        HttpURLConnection getCon = (HttpURLConnection) new URL(getUrl(urlStr)).openConnection();
        if (cookie != null) {
            getCon.setRequestProperty("Cookie", cookie);
        }
        getCon.connect();
        return StreamUtil.readStream(getCon.getInputStream()).toString();
    }
