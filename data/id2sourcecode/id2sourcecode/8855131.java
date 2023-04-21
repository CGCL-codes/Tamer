    public LociForm(IFormatReader reader, IFormatWriter writer) {
        super("LociForm" + formCount++);
        this.reader = reader;
        this.writer = writer;
    }
