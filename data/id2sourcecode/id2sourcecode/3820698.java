    protected DTO runInstanceMarshallingTest(final Marshaller marshal, final Unmarshaller unmarshal) throws InstantiationException, IllegalAccessException, XmlMappingException, IOException, IntrospectionException {
        Assert.isTrue(marshal.supports(_dtoClass), "Marshaller does not support DTO class");
        Assert.isTrue(unmarshal.supports(_dtoClass), "Unmarshaller does not support DTO class");
        final DTO orgDTO = createDTOInstance();
        final Writer writer = new StringWriter(1024);
        try {
            marshal.marshal(orgDTO, new StreamResult(writer));
        } finally {
            writer.close();
        }
        final Reader reader = new StringReader(writer.toString());
        final Object unmDTO;
        try {
            unmDTO = unmarshal.unmarshal(new StreamSource(reader));
        } finally {
            reader.close();
        }
        Assert.notNull(unmDTO, "No DTO unmarshalled");
        Assert.isInstanceOf(_dtoClass, unmDTO, "Unmarshalled DTO not compatible with DTO class");
        org.junit.Assert.assertNotSame("Identity marshalling", orgDTO, unmDTO);
        org.junit.Assert.assertEquals("Mismatched DTO contents", orgDTO, unmDTO);
        return orgDTO;
    }
