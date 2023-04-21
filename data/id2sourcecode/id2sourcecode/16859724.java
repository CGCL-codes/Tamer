    private void addDbMapping(IExtension extension) {
        try {
            IConfigurationElement conf = extension.getConfigurationElements()[0];
            Bundle resource = Platform.getBundle(conf.getDeclaringExtension().getNamespaceIdentifier());
            String sqlResource = conf.getAttribute("mappingFile");
            if (sqlResource != null) {
                URL url = resource.getEntry(sqlResource);
                InputStream fin = url.openStream();
                dbConfig.addInputStream(fin);
                fin.close();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
