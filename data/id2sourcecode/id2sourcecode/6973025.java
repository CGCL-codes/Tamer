    public ReaderWriterPersistanceImpl(Reader reader, Writer writer, String ticker) {
        super(ticker);
        this.reader = reader;
        this.writer = writer;
        sc = LaboratoryContext.getBean(BEANS.SYMBOLSCONFIG);
    }
