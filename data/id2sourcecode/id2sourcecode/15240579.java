    public InputStream getResourceAsStream(String name) {
        if (log.isDebugEnabled()) log.debug("getResourceAsStream(" + name + ")");
        InputStream stream = null;
        stream = findLoadedResource(name);
        if (stream != null) {
            if (log.isDebugEnabled()) log.debug("  --> Returning stream from cache");
            return (stream);
        }
        if (delegate) {
            if (log.isDebugEnabled()) log.debug("  Delegating to parent classloader " + parent);
            ClassLoader loader = parent;
            if (loader == null) loader = system;
            stream = loader.getResourceAsStream(name);
            if (stream != null) {
                if (log.isDebugEnabled()) log.debug("  --> Returning stream from parent");
                return (stream);
            }
        }
        if (log.isDebugEnabled()) log.debug("  Searching local repositories");
        URL url = findResource(name);
        if (url != null) {
            if (log.isDebugEnabled()) log.debug("  --> Returning stream from local");
            stream = findLoadedResource(name);
            try {
                if (hasExternalRepositories && (stream == null)) stream = url.openStream();
            } catch (IOException e) {
                ;
            }
            if (stream != null) return (stream);
        }
        if (!delegate) {
            if (log.isDebugEnabled()) log.debug("  Delegating to parent classloader unconditionally " + parent);
            ClassLoader loader = parent;
            if (loader == null) loader = system;
            stream = loader.getResourceAsStream(name);
            if (stream != null) {
                if (log.isDebugEnabled()) log.debug("  --> Returning stream from parent");
                return (stream);
            }
        }
        if (log.isDebugEnabled()) log.debug("  --> Resource not found, returning null");
        return (null);
    }
