    public static Reader getReader(String url) throws MalformedURLException, IOException {
        if (url.startsWith("http:")) return new InputStreamReader(new URL(url).openStream()); else return new FileReader(url);
    }
