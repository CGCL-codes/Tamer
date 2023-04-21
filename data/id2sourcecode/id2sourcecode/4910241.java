    private XmlForm elementDeclComplex(XSElementDecl decl, XSComplexType complexType) {
        if (log.isDebugEnabled()) log.debug("Complex Type: " + complexType);
        List<Form> forms = new ArrayList<Form>();
        Map<String, TextWriter> attrWriters = new LinkedHashMap<String, TextWriter>();
        Map<String, TextHandler> attrReaders = new LinkedHashMap<String, TextHandler>();
        Collection<? extends XSAttributeUse> attrs = complexType.getAttributeUses();
        attributes(forms, attrWriters, attrReaders, attrs);
        XmlWriter bodyWriter = null;
        XmlReader bodyReader = null;
        {
            XSContentType contentType = complexType.getContentType();
            XmlForm xmlForm = contentType.apply(this);
            if (xmlForm != null) {
                Form form = xmlForm.getForm();
                if (form instanceof FormSequence) forms.addAll(((FormSequence) form).getChildren()); else if (form != null) forms.add(form);
                bodyWriter = xmlForm.getWriter();
                bodyReader = xmlForm.getReader();
                if (bodyReader != null) bodyReader = WhitespaceReader.prependTo(bodyReader);
            }
        }
        Form form = FormSequenceImpl.newInstance(forms);
        if (form != null) form = new FormSectionImpl<Form>(decl.getName(), form);
        XmlWriter writer = new XmlElementWriter(decl.getName(), attrWriters, bodyWriter);
        XmlReader reader = new XmlElementReader(decl.getName(), attrReaders, bodyReader);
        if (ignoreWhitespace) reader = WhitespaceReader.appendTo(reader);
        return new XmlFormImpl(form, writer, reader);
    }
