    private CMLDictionary findDictionary() {
        CMLDictionary dictionary = null;
        String resourceS = "org/xmlcml/cml/converters/compchem/gaussian/gaussianArchiveDict.xml";
        URL url = getClass().getClassLoader().getResource(resourceS);
        if (url == null) {
            throw new RuntimeException("BUG: can't load gaussian archive dictionary (check resource directories are on the classpath: " + resourceS);
        }
        if (command != null && !command.isQuiet()) {
            LOG.info("URL " + url);
        }
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            CMLCml cml = (CMLCml) new CMLBuilder().build(inputStream).getRootElement();
            dictionary = (CMLDictionary) cml.getFirstCMLChild(CMLDictionary.TAG);
            if (dictionary == null) {
                throw new RuntimeException("Failed to find dictionary element in " + resourceS);
            }
        } catch (Exception e) {
            throw new RuntimeException("BUG: could not read/parse dictionary: " + resourceS, e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return dictionary;
    }
