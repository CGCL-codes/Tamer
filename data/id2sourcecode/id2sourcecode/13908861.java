    public Pattern validate(Parameters parameters) throws SAXException, JiBXException, IOException, KnittingEngineException {
        KnittingContext context = contextFactory.createKnittingContext();
        Pattern pattern = parameters.getPattern();
        Reader reader = parameters.getReader();
        if (pattern == null && reader == null) {
            throw new IllegalArgumentException("One of pattern or reader must be specified " + "in the Parameters object");
        }
        if (pattern == null) {
            if (parameters.isCheckSyntax()) {
                StringWriter writer = new StringWriter();
                IOUtils.copy(reader, writer);
                reader.close();
                writer.close();
                Source source = new StreamSource(new StringReader(writer.toString()));
                checkSyntax(source);
                reader = new StringReader(writer.toString());
            }
            IBindingFactory factory = BindingDirectory.getFactory(Pattern.class);
            IUnmarshallingContext uctx = factory.createUnmarshallingContext();
            pattern = (Pattern) uctx.unmarshalDocument(reader);
        }
        Visitor visitor = visitorFactory.findVisitorFromClassName(pattern);
        visitor.visit(pattern, context);
        Writer writer = parameters.getWriter();
        if (writer != null) {
            try {
                IBindingFactory factory = BindingDirectory.getFactory(Pattern.class);
                IMarshallingContext mctx = factory.createMarshallingContext();
                mctx.setOutput(writer);
                mctx.getXmlWriter().setIndentSpaces(2, null, ' ');
                mctx.getXmlWriter().writeXMLDecl("1.0", "UTF-8", null);
                mctx.marshalDocument(pattern);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        return pattern;
    }
