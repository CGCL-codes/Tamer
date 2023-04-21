    private static HttpURLConnection openConnection(String urlString) throws MalformedURLException, IOException, ClassCastException {
        return (HttpURLConnection) new URL(urlString).openConnection();
    }
