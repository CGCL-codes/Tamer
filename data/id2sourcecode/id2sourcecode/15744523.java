    @Test
    public void testParseCustomConverterParam() throws Exception {
        ResourceLoader loader = new ResourceLoader();
        URL url = loader.getResource("fieldCustomConverterParam.xml");
        Document document = XMLParserFactory.getInstance().createParser().parse(url.openStream());
        parser = new XMLParser(document);
        MappingFileData mappings = parser.load();
        assertNotNull("The mappings should not be null", mappings);
        List<ClassMap> mapping = mappings.getClassMaps();
        assertNotNull("The list of mappings should not be null", mapping);
        assertEquals("There should be one mapping", 3, mapping.size());
        ClassMap classMap = mapping.get(0);
        assertNotNull("The classmap should not be null", classMap);
        List<FieldMap> fieldMaps = classMap.getFieldMaps();
        assertNotNull("The fieldmaps should not be null", fieldMaps);
        assertEquals("The fieldmap should have one mapping", 1, fieldMaps.size());
        FieldMap fieldMap = fieldMaps.get(0);
        assertNotNull("The fieldmap should not be null", fieldMap);
        assertEquals("The customconverterparam should be correct", "CustomConverterParamTest", fieldMap.getCustomConverterParam());
    }
