    @SuppressWarnings("unused")
    public void testComplexDocument() throws Exception {
        Serializer serializer = new Persister();
        InputStream source = openDocument();
        int size = source.available();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read = 0;
        while ((read = source.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        source.close();
        byte[] data = baos.toByteArray();
        InputStream stream = new ByteArrayInputStream(data);
        long start = System.currentTimeMillis();
        CTDocument doc = serializer.read(CTDocument.class, stream, false);
        System.err.println("Time taken was " + (System.currentTimeMillis() - start));
        stream = new ByteArrayInputStream(data);
        start = System.currentTimeMillis();
        doc = serializer.read(CTDocument.class, stream, false);
        System.err.println("Second time taken was " + (System.currentTimeMillis() - start));
        List<WordXMLTagsOperation> bodyContentList = doc.getBody().getOperations();
        assertFalse(bodyContentList.isEmpty());
    }
