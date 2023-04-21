    public ExtentIndexWriter(TupleFlowParameters parameters) throws FileNotFoundException, IOException {
        writer = new IndexWriter(parameters);
        writer.getManifest().add("readerClass", ExtentIndexReader.class.getName());
        writer.getManifest().add("writerClass", getClass().toString());
        header = parameters.getXML();
    }
