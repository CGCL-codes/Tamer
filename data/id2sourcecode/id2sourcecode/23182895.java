    protected void copyData(Reader reader, Writer writer) throws IOException {
        super.copyData(new SplitLineReader(reader), new SplitLineWriter(writer));
    }
