    public void transform(Reader reader, Writer writer, Map<String, Object> parameterMap) {
        Util.transform(templates, schema, reader, writer, parameterMap);
    }
