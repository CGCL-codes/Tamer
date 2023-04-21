    private static StreamSource filter(String filter, StreamSource inSource) throws Exception {
        StreamSource src = new StreamSource(new FileInputStream(filter));
        Transformer trans = TransformerFactory.newInstance().newTransformer(src);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        StreamResult outResult = new StreamResult(bout);
        trans.transform(inSource, outResult);
        return new StreamSource(new ByteArrayInputStream(bout.toByteArray()));
    }
