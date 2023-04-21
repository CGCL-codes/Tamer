    public void testWithValueInSuperclass() {
        converterLookup.registerConverter(new ToAttributedValueConverter(OpenSourceSoftware.class, mapper, reflectionProvider, converterLookup, "name", Software.class), 0);
        final Software software = new OpenSourceSoftware("Codehaus", "XStream", "BSD");
        final StringWriter writer = new StringWriter();
        final CompactWriter compactWriter = new CompactWriter(writer);
        new TreeMarshaller(compactWriter, converterLookup, mapper).start(software, null);
        compactWriter.flush();
        assertEquals("<open-source vendor=\"Codehaus\" license=\"BSD\">XStream</open-source>", writer.toString());
        final HierarchicalStreamReader reader = new XppReader(new StringReader(writer.toString()));
        assertEquals(software, new TreeUnmarshaller(null, reader, converterLookup, mapper).start(null));
    }
