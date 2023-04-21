    public SparseFloatListWriter(TupleFlowParameters parameters) throws FileNotFoundException, IOException {
        writer = new IndexWriter(parameters);
        writer.getManifest().add("readerClass", SparseFloatListReader.class.getName());
        writer.getManifest().add("writerClass", getClass().getName());
    }
