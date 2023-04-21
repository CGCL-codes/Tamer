    protected CMLDictionary findDictionary(String resourceS) {
        dictionary = null;
        URL url = getClass().getClassLoader().getResource(resourceS);
        if (command != null && !command.isQuiet()) {
            LOG.info("URL " + url);
        }
        try {
            InputStream inputStream = url.openStream();
            CMLCml cml = (CMLCml) new CMLBuilder().build(inputStream).getRootElement();
            dictionary = (CMLDictionary) cml.getFirstCMLChild(CMLDictionary.TAG);
            if (dictionary == null) {
                throw new IllegalStateException("Failed to find dictionary element in " + resourceS);
            }
        } catch (Exception e) {
            LOG.warn("Failed to read dictionary from resource: " + url);
            throw new RuntimeException("bad dictionary: ", e);
        }
        return dictionary;
    }
