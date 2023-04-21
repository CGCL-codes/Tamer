    private XmlForm repeatFixed(Factory<XmlForm> factory, int count) {
        List<Form> forms = new ArrayList<Form>();
        List<XmlWriter> writers = new ArrayList<XmlWriter>();
        List<XmlReader> readers = new ArrayList<XmlReader>();
        if (ignoreWhitespace) readers.add(WhitespaceReader.getIInstance());
        for (int i = 0; i < count; i++) {
            XmlForm xmlForm = factory.create();
            if (xmlForm != null) {
                Form form = xmlForm.getForm();
                if (form != null) forms.add(form);
                XmlWriter writer = xmlForm.getWriter();
                if (writer != null) writers.add(writer);
                XmlReader reader = xmlForm.getReader();
                if (reader != null) readers.add(reader);
            }
        }
        Form form = FormSequenceImpl.newInstance(forms);
        XmlWriter writer = SequenceWriter.newInstance(writers);
        XmlReader reader = SequenceReader.newInstance(readers);
        return new XmlFormImpl(form, writer, reader);
    }
