    private static InputStream open(String url) throws MalformedURLException, IOException {
        if (FileUtils.isURI(url)) return new URL(url).openStream(); else return new FileInputStream(url);
    }
