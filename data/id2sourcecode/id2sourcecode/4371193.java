    protected URLConnection openConnection(URL url) throws MalformedURLException, IOException {
        return new JapURLConnection(url);
    }
