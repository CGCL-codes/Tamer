    private void loadFromURL(List<Schema> schemas, URL url) throws IOException, SchemaLoadingException {
        if (logger.isDebugEnabled()) logger.debug("Trying to load schema from " + url);
        URLConnection con = url.openConnection();
        if (con.getContentType().startsWith("application/octet-stream")) schemas.add(loadSchema(con.getInputStream()));
    }
