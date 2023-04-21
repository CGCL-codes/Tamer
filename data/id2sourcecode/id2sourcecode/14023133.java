    private URL asActualURL(URL url) throws IOException {
        if (!url.getProtocol().equals(PlatformURLHandler.PROTOCOL)) return url;
        URLConnection connection = url.openConnection();
        if (connection instanceof PlatformURLConnection) return ((PlatformURLConnection) connection).getResolvedURL();
        return url;
    }
