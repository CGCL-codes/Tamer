    private void testTransformer(String expectedDocumentName, String sourceDocumentName, SrxTransformer transformer, Map<String, Object> parameterMap) {
        Reader reader = getReader(getResourceStream(expectedDocumentName));
        String expectedDocument = removeWhitespaces(reader);
        reader = getReader(getResourceStream(sourceDocumentName));
        reader = transformer.transform(reader, parameterMap);
        String actualDocument = removeWhitespaces(reader);
        assertEquals(expectedDocument, actualDocument);
        reader = getReader(getResourceStream(sourceDocumentName));
        Writer writer = new StringWriter();
        transformer.transform(reader, writer, parameterMap);
        reader = new StringReader(writer.toString());
        actualDocument = removeWhitespaces(reader);
        assertEquals(expectedDocument, actualDocument);
    }
