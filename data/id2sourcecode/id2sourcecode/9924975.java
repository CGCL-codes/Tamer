    public static Properties getProperties(final String uri) {
        if (propsMap.containsKey(uri)) {
            return propsMap.get(uri);
        }
        final URL url = SystemResourceLoader.getInstance().findResource(uri);
        Log.info(ResourceProvider.class, "resource '" + uri + "' --> " + url);
        Properties props = new Properties();
        try {
            props.load(url.openStream());
        } catch (IOException e) {
            throw new ResourceException("Couldn't load image: " + uri, e);
        }
        propsMap.put(uri, props);
        return props;
    }
