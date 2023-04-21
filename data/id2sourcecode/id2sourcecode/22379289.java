    protected void testServices(final byte[] data1, final byte[] data2, final byte[] configData) {
        CompareProperties c = ServiceCreator.createTestService(CompareProperties.QNAME, XcdlCompareProperties.class, WSDL);
        DigitalObject first = new DigitalObject.Builder(Content.byValue(data1)).build();
        DigitalObject second = new DigitalObject.Builder(Content.byValue(data2)).build();
        DigitalObject configDigitalObject = new DigitalObject.Builder(Content.byValue(configData)).build();
        byte[] configBytes = FileUtils.writeInputStreamToBinary(configDigitalObject.getContent().read());
        File configFile = FileUtils.writeByteArrayToTempFile(configBytes);
        List<Parameter> configProps = new ArrayList<Parameter>(new ComparatorConfigParser(configFile).getProperties());
        List<Property> properties = new ArrayList<Property>(c.compare(c.convertInput(first), c.convertInput(second), configProps).getProperties());
        ComparatorWrapperTests.check(properties);
    }
