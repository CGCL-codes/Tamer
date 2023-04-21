    private URL tryURL(String contextPath, String path) throws MalformedURLException {
        URL ctx = new URL(contextPath);
        URL url = new URL(ctx, path);
        try {
            InputStream is = url.openStream();
            is.close();
        } catch (IOException e) {
            url = null;
        }
        return url;
    }
