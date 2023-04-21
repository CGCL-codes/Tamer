    protected HttpURLConnection open(String url) throws MalformedURLException, IOException {
        HttpsURLConnection hc = (HttpsURLConnection) new URL(url).openConnection();
        return hc;
    }
