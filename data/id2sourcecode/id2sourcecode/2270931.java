    public static FixtureSource newRemoteUrl(final URL url) {
        try {
            return new JSONSource(url.openStream());
        } catch (IOException e) {
            throw FixtureException.convert(e);
        }
    }
