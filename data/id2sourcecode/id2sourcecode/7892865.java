    protected void readInput(PipelineContext context, ExternalContext.Response response, ProcessorInput input, Object _config, OutputStream outputStream) {
        FileSerializer.Config config = (FileSerializer.Config) _config;
        Writer writer = getWriter(outputStream, config);
        TransformerHandler identity = TransformerUtils.getIdentityTransformerHandler();
        TransformerUtils.applyOutputProperties(identity.getTransformer(), config.getMethod(), config.getVersion(), config.getPublicDoctype(), config.getSystemDoctype(), config.getEncoding(), config.isOmitXMLDeclaration(), config.isStandalone(), config.isIndent(), config.getIndentAmount());
        identity.setResult(new StreamResult(writer));
        readInputAsSAX(context, input, new SerializerContentHandler(identity, writer, getPropertySet().getBoolean("serialize-xml-11", false).booleanValue()));
    }
