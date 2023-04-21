    public static InputStream openUriForReading(@NotNull final String uri) throws IOException, URISyntaxException {
        final URI theUri = new URI(uri);
        final URL url = theUri.toURL();
        return url.openStream();
    }
