    private boolean isCompatible(Bundle bundle, IPath prefixedPath) {
        URL url = FileLocator.find(bundle, prefixedPath.append(SearchIndex.DEPENDENCIES_VERSION_FILENAME), null);
        if (url == null) {
            HelpBasePlugin.logError(prefixedPath.append(SearchIndex.DEPENDENCIES_VERSION_FILENAME) + " file missing from help index \"" + path + "\" of plugin " + getPluginId(), null);
            return false;
        }
        InputStream in = null;
        try {
            in = url.openStream();
            Properties prop = new Properties();
            prop.load(in);
            String lucene = prop.getProperty(SearchIndex.DEPENDENCIES_KEY_LUCENE);
            String analyzer = prop.getProperty(SearchIndex.DEPENDENCIES_KEY_ANALYZER);
            if (!targetIndex.isLuceneCompatible(lucene) || !targetIndex.isAnalyzerCompatible(analyzer)) {
                return false;
            }
        } catch (MalformedURLException mue) {
            return false;
        } catch (IOException ioe) {
            HelpBasePlugin.logError("IOException accessing prebuilt index.", ioe);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return true;
    }
