    public void load(URL url) throws IOException, ModelFormatException {
        if (scene.getName() == null) {
            String urlPath = url.getPath();
            if (urlPath == null) {
                throw new IOException("URL contains no path: " + url);
            }
            String sceneName = urlPath.replaceFirst(".*[\\\\/]", "").replaceFirst("\\..*", "");
            if (!sceneName.matches(".*(?i)scene.*")) {
                sceneName += "Scene";
            }
            if (sceneName.length() < 1) {
                logger.warning("Falling back to default scene name, since " + "failed to generate a good name from URL '" + url + "'");
            } else {
                scene.setName(sceneName);
            }
        }
        ResourceLocator locator = null;
        try {
            locator = new RelativeResourceLocator(url);
        } catch (URISyntaxException use) {
            throw new IllegalArgumentException("Bad URL: " + use);
        }
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_MODEL, locator);
        InputStream stream = null;
        try {
            stream = url.openStream();
            if (stream == null) {
                throw new IOException("Failed to load model file '" + url + "'");
            }
            logger.fine("Loading materials from '" + url + "'...");
            load(stream);
            stream.close();
        } finally {
            if (stream != null) stream.close();
            ResourceLocatorTool.removeResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
            ResourceLocatorTool.removeResourceLocator(ResourceLocatorTool.TYPE_MODEL, locator);
            locator = null;
        }
    }
