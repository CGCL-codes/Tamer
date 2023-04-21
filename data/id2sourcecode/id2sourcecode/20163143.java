    public static void transform(Reader reader, String xsltString, Writer writer) throws TransformerException {
        Transformer transformer = getTransformer(xsltString);
        transformer.transform(new StreamSource(reader), new StreamResult(writer));
    }
