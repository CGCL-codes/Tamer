    public ParagraphInserter(IDatumFactory datumFactory, IWriterFactory writerFactory, IReaderFactory readerFactory, IParagraphInserterFactory inserterFactory, IParagraphInserterStateMachineFactory stateMachineFactory) {
        myDatumFactory = datumFactory;
        myWriterFactory = writerFactory;
        myReaderFactory = readerFactory;
        myInserterFactory = inserterFactory;
        myStateMachineFactory = stateMachineFactory;
    }
