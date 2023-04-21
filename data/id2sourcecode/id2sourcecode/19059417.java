    protected HttpURLConnection open(String url) throws MalformedURLException, IOException {
        return (HttpURLConnection) (new URL(url)).openConnection();
    }
