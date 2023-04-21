    private static int getContentLength(String location) throws MalformedURLException, IOException {
        URL url = new URL(location);
        return url.openConnection().getContentLength();
    }
