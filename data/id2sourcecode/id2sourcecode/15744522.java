    @Test
    public void testParse() throws Exception {
        ResourceLoader loader = new ResourceLoader();
        URL url = loader.getResource("dozerBeanMapping.xml");
        Document document = XMLParserFactory.getInstance().createParser().parse(url.openStream());
        parser = new XMLParser(document);
        MappingFileData mappings = parser.load();
        assertNotNull(mappings);
    }
