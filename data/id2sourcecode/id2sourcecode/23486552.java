    public void createIndex(int indexType, ByteBuffer data) throws IndexException, IllegalStateException {
        if (!editMode) throw new IllegalStateException();
        if (hasIndex(indexType)) throw new IndexException("Index data of type " + indexType + " already exists");
        try {
            indexFile.seek(indexFile.length());
            IndexMetaData index = new IndexMetaData(indexType, indexFile, data.remaining());
            indexFile.getChannel().write(data);
            indexes.add(index);
        } catch (IOException ex) {
            throw new IndexException(ex);
        }
    }
