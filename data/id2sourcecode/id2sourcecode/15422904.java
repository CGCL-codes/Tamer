    public static ModelInstance loadGraph(URL url) {
        assert (url != null);
        InputStream in = null;
        try {
            in = url.openStream();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException while opening a URL stream to load: {0}\n{1}", new Object[] { url, ex });
        }
        ModelInstance result = null;
        if (in != null) result = loadGraph(in); else logger.log(Level.SEVERE, "Failed loading graph from url: {0}", url);
        return result;
    }
