    protected List<DTO> runInstanceListMarshallingTest(final Marshaller marshal, final Unmarshaller unmarshal) throws InstantiationException, IllegalAccessException, InvocationTargetException, XmlMappingException, IOException, IntrospectionException {
        Assert.isTrue(marshal.supports(_listClass), "Marshaller does not support DTO list class");
        Assert.isTrue(unmarshal.supports(_listClass), "Unmarshaller does not support DTO list class");
        final int MAX_ITEMS = Byte.SIZE;
        final L dtoList = _sizedConstructor.newInstance(Integer.valueOf(MAX_ITEMS));
        for (int index = 0; index < MAX_ITEMS; index++) {
            dtoList.add(createDTOInstance());
        }
        final Writer writer = new StringWriter(1024);
        try {
            marshal.marshal(dtoList, new StreamResult(writer));
        } finally {
            writer.close();
        }
        final Reader reader = new StringReader(writer.toString());
        final Object unmList;
        try {
            unmList = unmarshal.unmarshal(new StreamSource(reader));
        } finally {
            reader.close();
        }
        Assert.notNull(unmList, "No list unmarshalled");
        Assert.isInstanceOf(List.class, unmList, "Unmarshalled object not a list");
        org.junit.Assert.assertNotSame("Identity marshalling", dtoList, unmList);
        final List<?> resList = (List<?>) unmList;
        org.junit.Assert.assertEquals("Mismatched list sizes", dtoList.size(), resList.size());
        if (!CollectionUtils.isEqualCollection(dtoList, resList)) org.junit.Assert.fail("Mismatched lists contents - expected: " + dtoList + ", actual: " + resList);
        return dtoList;
    }
