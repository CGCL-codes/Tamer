    private XmlForm elementDeclSimple(XSElementDecl decl, XSSimpleType simpleType) {
        FormElement element = declSimple(simpleType, decl.getName(), decl.getDefaultValue(), decl.getFixedValue());
        if (element == null) return null;
        XmlWriter writer = new XmlElementWriter(decl.getName(), null, new TextXmlWriter(new FormElementWriter(element)));
        XmlReader reader = new XmlElementReader(decl.getName(), null, new TextXmlReader(new FormElementHandler(element)));
        if (ignoreWhitespace) reader = WhitespaceReader.appendTo(reader);
        return new XmlFormImpl(element, writer, reader);
    }
