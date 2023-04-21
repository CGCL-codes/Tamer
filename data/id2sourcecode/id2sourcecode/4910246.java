    private XmlForm repeatDynamic(Factory<XmlForm> factory, Integer min, Integer max) {
        RepeatAdapterImpl adapter = new RepeatAdapterImpl(factory);
        FormRepeat<Form> form = new FormRepeatImpl<Form>(adapter, min, max);
        adapter.init(form);
        XmlWriter writer = new RepeatWriter(adapter);
        XmlReader reader = new RepeatReader(form, adapter);
        if (ignoreWhitespace) reader = WhitespaceReader.prependTo(reader);
        for (int i = 0; i < min; i++) form.add();
        form.setHeaderFactory(new SequenceHeaderFactory(form));
        return new XmlFormImpl(form, writer, reader);
    }
