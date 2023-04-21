    public void testWithValueInConvertedClass() {
        converterLookup.registerConverter(new ToAttributedValueConverter(Software.class, mapper, reflectionProvider, converterLookup, "name"), 0);
        final Software name = new Software(null, "XStream");
        final StringWriter writer = new StringWriter();
        final CompactWriter compactWriter = new CompactWriter(writer);
        new TreeMarshaller(compactWriter, converterLookup, mapper).start(name, null);
        compactWriter.flush();
        assertEquals("<software>XStream</software>", writer.toString());
        final HierarchicalStreamReader reader = new XppReader(new StringReader(writer.toString()));
        assertEquals(name, new TreeUnmarshaller(null, reader, converterLookup, mapper).start(null));
    }
