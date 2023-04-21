    public XmlForm attributeDecl(XSAttributeDecl decl) {
        if (log.isDebugEnabled()) log.debug("Attribute Declaration: " + decl);
        FormElement element = declSimple(decl.getType(), decl.getName(), decl.getDefaultValue(), decl.getFixedValue());
        if (element == null) return null;
        XmlWriter writer = new TextXmlWriter(new FormElementWriter(element));
        XmlReader reader = new TextXmlReader(new FormElementHandler(element));
        return new XmlFormImpl(element, writer, reader);
    }
