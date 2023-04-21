    public ByteBuffer getIndexData(int indexType) throws IndexException, IllegalStateException {
        if (editMode) throw new IllegalStateException();
        IndexMetaData index = getIndexMetaData(indexType);
        if (index == null) throw new IndexException("No index data of type " + indexType + " available");
        try {
            return index.getIndexData(indexFile.getChannel());
        } catch (IOException ex) {
            throw new IndexException(ex);
        }
    }
