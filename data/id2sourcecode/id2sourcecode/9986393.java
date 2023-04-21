    public PseudoTerminal(Reader reader, Writer writer) {
        this.reader = reader;
        this.writer = writer;
        for (int i = 0; i < DEFAULT_ATTRIBUTES.length; i++) {
            attributes[DEFAULT_ATTRIBUTES[i]] = 1;
        }
    }
