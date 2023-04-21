    private void loadDefaultFormats() {
        String formatSchemaLocation = resourceHandler.getFileLocation("resources/formats.xsd");
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint("org.deft.repository.formats");
        for (IConfigurationElement defaultFormat : point.getConfigurationElements()) {
            String parserID = defaultFormat.getAttribute("parserID");
            String filePath = defaultFormat.getAttribute("xmlFile");
            String plugin = defaultFormat.getContributor().getName();
            XmlToFormatContentConverter fConvDefault = new XmlToFormatContentConverter(this, formatSchemaLocation, parserID);
            try {
                URL url = EclipsePluginResourceHandler.getFileLocation(plugin, filePath);
                InputStream is = url.openStream();
                fConvDefault.convert(is);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
