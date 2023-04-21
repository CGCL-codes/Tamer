    private XComponent loadDocumentAsOOoModel(String path) throws Exception {
        LOG.debug("Opening " + path);
        PropertyValue[] loaderValues = createDocumentLoadingProps();
        XComponent document = null;
        File f = new File(path);
        try {
            document = _documentLoader.loadComponentFromURL(getSunURLForFile(f), "_blank", 0, loaderValues);
            LOG.debug("Opened " + path);
        } catch (final com.sun.star.lang.IllegalArgumentException iae) {
            final File temp = File.createTempFile("ooo", "tmp");
            temp.deleteOnExit();
            FileUtils.copyFile(f, temp);
            document = _documentLoader.loadComponentFromURL(getSunURLForFile(temp), "_blank", 0, loaderValues);
        }
        return document;
    }
