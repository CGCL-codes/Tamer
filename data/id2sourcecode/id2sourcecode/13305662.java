    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        if (url.toExternalForm().startsWith(JarBrowser.INNER_JAR_PROTOCOL + JarBrowser.PROTOCOL_DELIMITER)) {
            return new InnerJarUrlConnection(url);
        } else {
            return url.openConnection();
        }
    }
