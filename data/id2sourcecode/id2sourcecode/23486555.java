    public void deleteIndex(int indexType) throws IndexException, IllegalStateException {
        if (!editMode) throw new IllegalStateException();
        IndexMetaData index = getIndexMetaData(indexType);
        if (index == null) throw new IndexException("No index data of type " + indexType + " available");
        FileChannel indexChannel = indexFile.getChannel();
        try {
            long remainder = indexChannel.size() - index.nextIndexMetaDataOffset();
            if (remainder > 0) {
                indexChannel.position(index.getHeaderOffset());
                indexChannel.transferFrom(indexChannel, index.nextIndexMetaDataOffset(), remainder);
                indexChannel.truncate(index.getHeaderOffset() + remainder);
            }
            readIndexMetaData();
        } catch (IOException ex) {
            throw new IndexException(ex);
        }
    }
