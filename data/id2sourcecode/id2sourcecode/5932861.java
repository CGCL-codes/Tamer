    public void testWillDistinguishBetweenDifferentTypes() {
        converterLookup.registerConverter(new ToAttributedValueConverter(Software.class, mapper, reflectionProvider, converterLookup, "name"), 0);
        converterLookup.registerConverter(new ToAttributedValueConverter(OpenSourceSoftware.class, mapper, reflectionProvider, converterLookup, "license"), 0);
        final Software[] software = new Software[] { new Software("Microsoft", "Windows"), new OpenSourceSoftware("Codehaus", "XStream", "BSD") };
        final StringWriter writer = new StringWriter();
        final PrettyPrintWriter prettyPrintWriter = new PrettyPrintWriter(writer);
        new TreeMarshaller(prettyPrintWriter, converterLookup, mapper).start(software, null);
        prettyPrintWriter.flush();
        assertEquals("" + "<software-array>\n" + "  <software vendor=\"Microsoft\">Windows</software>\n" + "  <open-source vendor=\"Codehaus\" name=\"XStream\">BSD</open-source>\n" + "</software-array>", writer.toString());
        final HierarchicalStreamReader reader = new XppReader(new StringReader(writer.toString()));
        Software[] array = (Software[]) new TreeUnmarshaller(null, reader, converterLookup, mapper).start(null);
        assertEquals(software[0], array[0]);
        assertEquals(software[1], array[1]);
    }
