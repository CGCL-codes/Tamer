    public void transform(Reader reader, Writer writer, Map<String, Object> parameterMap) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        SrxTransformer transformer = getTransformer(bufferedReader);
        transformer.transform(bufferedReader, writer, parameterMap);
    }
