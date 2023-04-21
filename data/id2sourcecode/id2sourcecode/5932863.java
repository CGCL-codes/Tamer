    public void testWithoutValueField() {
        converterLookup.registerConverter(new ToAttributedValueConverter(Software.class, mapper, reflectionProvider, converterLookup, null), 0);
        final Software software = new Software("Codehaus", "XStream");
        final StringWriter writer = new StringWriter();
        final CompactWriter compactWriter = new CompactWriter(writer);
        new TreeMarshaller(compactWriter, converterLookup, mapper).start(software, null);
        compactWriter.flush();
        assertEquals("<software vendor=\"Codehaus\" name=\"XStream\"/>", writer.toString());
        final HierarchicalStreamReader reader = new XppReader(new StringReader(writer.toString()));
        assertEquals(software, new TreeUnmarshaller(null, reader, converterLookup, mapper).start(null));
    }
