    public static String readUrl(final URL urlToReadFrom) throws IOException {
        assert urlToReadFrom != null;
        final InputStream in = urlToReadFrom.openStream();
        return readAndClose(in);
    }
