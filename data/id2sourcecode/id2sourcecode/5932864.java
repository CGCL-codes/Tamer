    public void testWithComplexValueField() {
        converterLookup.registerConverter(new ToAttributedValueConverter(X.class, mapper, reflectionProvider, converterLookup, "innerObj"), 0);
        final X x = new X(42);
        x.aStr = "xXx";
        x.innerObj = new Y();
        x.innerObj.yField = "inner";
        final StringWriter writer = new StringWriter();
        final CompactWriter compactWriter = new CompactWriter(writer);
        new TreeMarshaller(compactWriter, converterLookup, mapper).start(x, null);
        compactWriter.flush();
        assertEquals("<x aStr=\"xXx\" anInt=\"42\"><yField>inner</yField></x>", writer.toString());
        final HierarchicalStreamReader reader = new XppReader(new StringReader(writer.toString()));
        assertEquals(x, new TreeUnmarshaller(null, reader, converterLookup, mapper).start(null));
    }
