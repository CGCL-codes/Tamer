    public static void forEachLine(final URL url, final LineListener lit) {
        try {
            ReaderUtils.forEachLine(url.openStream(), lit);
        } catch (final IOException ioe) {
            lit.exception(ioe);
        }
    }
