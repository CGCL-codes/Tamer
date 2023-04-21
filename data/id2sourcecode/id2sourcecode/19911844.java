    public static void transform(Templates templates, Schema schema, Reader reader, Writer writer, Map<String, Object> parameterMap) {
        try {
            Source source = getSource(reader, schema);
            Result result = new StreamResult(writer);
            Transformer transformer = templates.newTransformer();
            transformer.setErrorListener(new TransformationErrorListener());
            for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
                transformer.setParameter(entry.getKey(), entry.getValue());
            }
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            throw new XmlException("Error creating XSLT transformer.", e);
        } catch (TransformerException e) {
            throw new XmlException("XSLT transformer error.", e);
        }
    }
