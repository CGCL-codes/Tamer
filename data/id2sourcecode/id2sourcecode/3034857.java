    @Test
    public void testMarshall() throws JiBXException, SAXException, IOException {
        MetadataPermissions o = MetadataPermissionsFactory.create();
        IBindingFactory bfact = BindingDirectory.getFactory(MetadataPermissions.class);
        IMarshallingContext marshallingContext = bfact.createMarshallingContext();
        Writer outConsole = new BufferedWriter(new OutputStreamWriter(System.out));
        marshallingContext.setOutput(outConsole);
        marshallingContext.setIndent(3);
        marshallingContext.marshalDocument(o, "UTF-8", null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = new BufferedWriter(new OutputStreamWriter(outputStream));
        marshallingContext.setIndent(3);
        marshallingContext.setOutput(out);
        marshallingContext.marshalDocument(o, "UTF-8", null);
        InputSource marshallingResult = new InputSource(new ByteArrayInputStream(outputStream.toByteArray()));
        FileInputStream fis = new FileInputStream(new File("src/test/resources/MetadataPermissionsTestData.xml"));
        InputSource expectedResult = new InputSource(fis);
        DifferenceListener differenceListener = new IgnoreTextAndAttributeValuesDifferenceListener();
        Diff diff = new Diff(expectedResult, marshallingResult);
        diff.overrideDifferenceListener(differenceListener);
        assertTrue("Marshalled MetadataPermissions matches expected XML " + diff, diff.similar());
    }
