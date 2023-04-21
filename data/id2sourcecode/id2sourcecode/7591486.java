    public String get(String path, JPublishContext context) throws Exception {
        if (log.isDebugEnabled()) log.debug("Getting dynamic content element for path " + path);
        StringWriter writer = null;
        StringReader reader = null;
        try {
            writer = new StringWriter();
            reader = new StringReader(get(path));
            String name = PathUtilities.makeRepositoryURI(getName(), path);
            ViewRenderer renderer = siteContext.getViewRenderer();
            renderer.render(context, name, reader, writer);
            return writer.toString();
        } finally {
            IOUtilities.close(writer);
            IOUtilities.close(reader);
        }
    }
