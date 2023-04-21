    public static URLConnection openConnection(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        return openConnection(url);
    }
