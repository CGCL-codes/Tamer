    private void loadDefaultFormats() {
        String formatSchemaLocation = resourceHandler.getFileLocation("resources/formats.xsd");
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint("DepthRepositoryPlugIn.formats");
        for (IConfigurationElement defaultFormat : point.getConfigurationElements()) {
            String parserID = defaultFormat.getAttribute("parserID");
            String filePath = defaultFormat.getAttribute("xmlFile");
            String plugin = defaultFormat.getContributor().getName();
            XmlToFormatContentConverter fConvDefault = new XmlToFormatContentConverter(this, formatSchemaLocation, parserID);
            try {
                URL url = EclipsePluginResourceHandler.getFileLocation(plugin, filePath);
                fConvDefault.convert(url.openStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
