    public Configuration addURL(URL url) throws MappingException {
        if (log.isDebugEnabled()) {
            log.debug("Reading mapping document from URL:" + url.toExternalForm());
        }
        try {
            addInputStream(url.openStream());
        } catch (InvalidMappingException e) {
            throw new InvalidMappingException("URL", url.toExternalForm(), e.getCause());
        } catch (Exception e) {
            throw new InvalidMappingException("URL", url.toExternalForm(), e);
        }
        return this;
    }
