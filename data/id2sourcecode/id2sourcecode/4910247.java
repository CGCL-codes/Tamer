    public XmlForm modelGroup(XSModelGroup group) {
        List<Form> forms = new ArrayList<Form>();
        List<XmlWriter> writers = new ArrayList<XmlWriter>();
        List<XmlReader> readers = new ArrayList<XmlReader>();
        XSParticle[] children = group.getChildren();
        for (int i = 0; i < children.length; i++) {
            XmlForm xmlForm = children[i].apply(this);
            if (xmlForm != null) {
                Form form = xmlForm.getForm();
                if (form != null) {
                    forms.add(form);
                    writers.add(xmlForm.getWriter());
                    readers.add(xmlForm.getReader());
                }
            }
        }
        Compositor compositor = group.getCompositor();
        if (log.isDebugEnabled()) log.debug("Compositor: " + compositor);
        Form form;
        XmlWriter writer;
        XmlReader reader;
        switch(compositor) {
            case SEQUENCE:
                form = FormSequenceImpl.newInstance(forms, true);
                writer = SequenceWriter.newInstance(writers);
                reader = SequenceReader.newInstance(readers);
                break;
            case CHOICE:
                FormChoice choice = new FormChoiceImpl(forms);
                writer = new ChoiceWriter(choice, writers);
                reader = new ChoiceReader(choice, readers);
                form = choice;
                break;
            case ALL:
                FormAll all = new FormAllImpl(forms, true);
                writer = new AllWriter(all, writers);
                reader = new AllReader(all, readers);
                form = all;
                break;
            default:
                throw new AssertionError("Unknown Compositor: " + compositor);
        }
        if (ignoreWhitespace) reader = WhitespaceReader.appendTo(reader);
        return new XmlFormImpl(form, writer, reader);
    }
