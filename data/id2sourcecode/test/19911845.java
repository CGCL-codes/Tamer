    public static void transform(Templates templates, Schema schema, Reader reader, Writer writer) {
        Map<String, Object> parameterMap = Collections.emptyMap();
        transform(templates, schema, reader, writer, parameterMap);
    }
