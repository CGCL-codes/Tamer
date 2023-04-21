    public String get(String path, JPublishContext context) throws Exception {
        if (log.isDebugEnabled()) log.debug("Getting dynamic content element for path " + path);
        StringWriter writer = null;
        BufferedReader reader = null;
        Reader in = null;
        try {
            in = new StringReader(loadContent(path));
            reader = new BufferedReader(in);
            writer = new StringWriter();
            String name = PathUtilities.makeRepositoryURI(getName(), path);
            ViewRenderer renderer = siteContext.getViewRenderer();
            renderer.render(context, name, reader, writer);
            return writer.toString();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            throw new FileNotFoundException("File not found: " + path);
        } finally {
            IOUtilities.close(reader);
            IOUtilities.close(writer);
            IOUtilities.close(in);
        }
    }
