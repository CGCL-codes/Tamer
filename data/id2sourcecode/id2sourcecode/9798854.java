    @Override
    public InputStream getResourceAsStream(String name) {
        InputStream in = null;
        if (!childFirstResources) {
            in = getParent().getResourceAsStream(name);
            if (in != null) return in;
        }
        URL url = findResource(name);
        if (url != null) try {
            return url.openStream();
        } catch (IOException ignored) {
        }
        if (childFirstResources) in = getParent().getResourceAsStream(name);
        return in;
    }
