    @Test
    public void testWriteReadBean() {
        Writer writer = null;
        Reader reader = null;
        writer = new StringWriter();
        BeanXmlMapper.writeBean(writer, this.testStateSpaceDTO);
        reader = new StringReader(writer.toString());
        TestStateSpaceDTO testStateSpaceDTO = (TestStateSpaceDTO) BeanXmlMapper.readBean(reader, "TestStateSpaceDTO", TestStateSpaceDTO.class);
        assertEquals("testStateSpaceDTO failed to map from object to xml and back.", this.testStateSpaceDTO, testStateSpaceDTO);
    }
