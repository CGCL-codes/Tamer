    protected void readInput(PipelineContext context, ProcessorInput input, Config config, Writer writer) {
        TransformerHandler identity = TransformerUtils.getIdentityTransformerHandler();
        TransformerUtils.applyOutputProperties(identity.getTransformer(), config.method != null ? config.method : DEFAULT_METHOD, null, null, null, getEncoding(config, null, DEFAULT_ENCODING), true, null, false, DEFAULT_INDENT_AMOUNT);
        identity.setResult(new StreamResult(writer));
        readInputAsSAX(context, INPUT_DATA, new SerializerContentHandler(identity, writer, isSerializeXML11()));
    }
