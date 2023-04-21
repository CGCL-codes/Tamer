    public HttpURLConnection getHttpConnection(String urlString) throws IOException {
        try {
            URL url = new URL(urlString);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException exc) {
            throw new RuntimeException("Configured URL caused a MalformedURLException: ", exc);
        }
    }
