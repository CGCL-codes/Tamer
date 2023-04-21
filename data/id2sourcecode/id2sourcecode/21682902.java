    private InputStream openMyTestStream(final String filename) {
        try {
            URL installURL = FreqAnalysisPlugin.getDefault().getBundle().getEntry("/");
            URL url = new URL(installURL, filename);
            return (url.openStream());
        } catch (MalformedURLException e) {
            LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e);
        } catch (IOException e) {
            LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e);
        }
        return null;
    }
