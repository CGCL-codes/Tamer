    protected void readInput(PipelineContext context, ProcessorInput input, Config config, Writer writer) {
        TransformerHandler identity = TransformerUtils.getIdentityTransformerHandler();
        TransformerUtils.applyOutputProperties(identity.getTransformer(), config.method != null ? config.method : DEFAULT_METHOD, config.version != null ? config.version : null, config.publicDoctype != null ? config.publicDoctype : null, config.systemDoctype != null ? config.systemDoctype : null, getEncoding(config, null, DEFAULT_ENCODING), config.omitXMLDeclaration, config.standalone, config.indent, config.indentAmount);
        identity.setResult(new StreamResult(writer));
        ProcessorImpl.readInputAsSAX(context, input, new StripNamespaceContentHandler(identity, writer, isSerializeXML11()));
    }
