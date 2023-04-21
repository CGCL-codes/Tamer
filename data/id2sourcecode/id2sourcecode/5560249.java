    public InputStream getResourceAsStream(String uriInContext) {
        if (uriInContext == null || !uriInContext.startsWith("/")) return null;
        try {
            Resource resource = getHttpContext().getResource(uriInContext);
            if (resource != null) return resource.getInputStream();
            uriInContext = URI.canonicalPath(uriInContext);
            URL url = getResource(uriInContext);
            if (url != null) return url.openStream();
        } catch (IOException e) {
            LogSupport.ignore(log, e);
        }
        return null;
    }
