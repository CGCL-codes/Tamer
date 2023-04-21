    private static InputStream loadFromUrl(String urlPath) throws IOException {
        log.debug("Opening url {}", urlPath);
        URL url = new URL(urlPath);
        return url.openStream();
    }
