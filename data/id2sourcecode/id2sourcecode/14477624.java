    public CSVParser(Reader reader, OutputStream writer, int skipLines) throws CSVException {
        _csvLines = new Vector();
        _writer = writer;
        _skipLines = skipLines;
        _reader = reader;
        parseCSV();
    }
