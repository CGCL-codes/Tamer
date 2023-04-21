    public OgreEntityNode loadModel(URL url, String nodeName) throws IOException, ModelFormatException {
        logger.fine("MESH(" + url.getFile() + ")");
        String name = null;
        if (nodeName == null) {
            String urlPath = url.getPath();
            if (urlPath == null) {
                throw new IOException("URL contains no path: " + url);
            }
            name = urlPath.replaceFirst(".*[\\\\/]", "").replaceFirst("\\..*", "");
            if (name.length() < 1) {
                name = "OgreNode";
                logger.warning("Falling back to node name 'OgreNode', since " + "failed to generate a good name from URL '" + url + "'");
            }
        } else {
            name = nodeName;
        }
        ResourceLocator locator = null;
        try {
            locator = new RelativeResourceLocator(url);
        } catch (URISyntaxException use) {
            throw new RuntimeException(use);
        }
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_MODEL, locator);
        InputStream stream = null;
        try {
            return loadMesh(loadDocument(url.openStream(), "mesh"), name);
        } finally {
            ResourceLocatorTool.removeResourceLocator(ResourceLocatorTool.TYPE_MODEL, locator);
            locator = null;
        }
    }
