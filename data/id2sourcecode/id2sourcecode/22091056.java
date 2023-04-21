    @Override
    protected long getLastModified(HttpServletRequest req) {
        String resourcePath = getResourcePath(req);
        URL url = getClass().getResource(resourcePath);
        if (url == null) return -1;
        try {
            long lastModified = url.openConnection().getLastModified();
            return (lastModified == 0) ? -1 : lastModified;
        } catch (Exception xp) {
            return -1;
        }
    }
