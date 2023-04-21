    public D readDao(final URL url, final CtuluLog analyser, final CrueData dataLinked) {
        if (url == null) {
            analyser.addFatalError("file.url.null.error");
            return null;
        }
        InputStream in = null;
        D newData = null;
        try {
            in = url.openStream();
            newData = readDao(in, analyser, dataLinked);
        } catch (final IOException e) {
            LOGGER.log(Level.FINE, e.getMessage(), e);
            analyser.addFatalError("io.xml.error", e.getMessage());
        } finally {
            CtuluLibFile.close(in);
        }
        return newData;
    }
