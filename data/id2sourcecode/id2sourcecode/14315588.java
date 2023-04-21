    protected PoliticsText() {
        final ResourceLoader loader = ResourceLoader.getMapResourceLoader(UIContext.getMapDir());
        final URL url = loader.getResource(PROPERTY_FILE);
        if (url == null) {
        } else {
            try {
                m_properties.load(url.openStream());
            } catch (final IOException e) {
                System.out.println("Error reading " + PROPERTY_FILE + " : " + e);
            }
        }
    }
