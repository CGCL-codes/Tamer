    protected CMLDictionary findDictionary(String resourceS) {
        compchemDict = null;
        URL url = getClass().getClassLoader().getResource(resourceS);
        if (url == null) {
            throw new RuntimeException("cannot find dictionary: " + resourceS);
        }
        try {
            InputStream inputStream = url.openStream();
            CMLCml cml = (CMLCml) new CMLBuilder().build(inputStream).getRootElement();
            compchemDict = (CMLDictionary) cml.getFirstCMLChild(CMLDictionary.TAG);
            if (compchemDict == null) {
                throw new IllegalStateException("Failed to find dictionary element in " + resourceS);
            }
        } catch (Exception e) {
            LOG.warn("Failed to read dictionary from resource: " + url);
            throw new RuntimeException("bad dictionary: ", e);
        }
        return compchemDict;
    }
