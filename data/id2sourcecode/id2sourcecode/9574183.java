    public PositionIndexWriter(TupleFlowParameters parameters) throws FileNotFoundException, IOException {
        writer = new IndexWriter(parameters);
        writer.getManifest().add("writerClass", getClass().getName());
        writer.getManifest().add("readerClass", PositionIndexReader.class.getName());
    }
