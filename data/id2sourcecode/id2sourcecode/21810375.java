    private static HttpURLConnection connect(String url) throws IOException, MalformedURLException {
        return (HttpURLConnection) new URL(url).openConnection();
    }
