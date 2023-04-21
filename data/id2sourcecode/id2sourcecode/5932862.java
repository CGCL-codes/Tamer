    public void testWithNullValueDeserializedAsEmptyString() {
        converterLookup.registerConverter(new ToAttributedValueConverter(Software.class, mapper, reflectionProvider, converterLookup, "name"), 0);
        final Software software = new Software(null, null);
        final StringWriter writer = new StringWriter();
        final CompactWriter compactWriter = new CompactWriter(writer);
        new TreeMarshaller(compactWriter, converterLookup, mapper).start(software, null);
        compactWriter.flush();
        assertEquals("<software/>", writer.toString());
        final HierarchicalStreamReader reader = new XppReader(new StringReader(writer.toString()));
        assertEquals("", ((Software) new TreeUnmarshaller(null, reader, converterLookup, mapper).start(null)).name);
    }
