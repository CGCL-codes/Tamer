    public void testParse() throws Exception {
        XMLParser parser = new XMLParser();
        ResourceLoader loader = new ResourceLoader();
        URL url = loader.getResource("dozerBeanMapping.xml");
        Mappings mappings = parser.parse(url.openStream());
        assertNotNull(mappings);
    }
