    public TeeReader(Reader reader, Writer writer) {
        super(reader);
        Validations.notNull(writer, "writer");
        this.writer = writer;
    }
