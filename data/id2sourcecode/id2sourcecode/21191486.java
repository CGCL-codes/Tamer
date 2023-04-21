    public void addIndex(String indexName, IndexLocation source) throws IOException, KattaException {
        if (indexes.hasIndex(indexName)) {
            copyRemoteIndex(source);
            IndexReader reader = null;
            IndexWriter writer = null;
            try {
                writer = openNewIndexVersion(indexName);
                File dir = indexes.getKnownIndexDirectory(source.getIndexVersion());
                reader = getIndexReader(dir);
                IndexReader[] readers = new IndexReader[1];
                readers[0] = reader;
                writer.addIndexes(readers);
            } finally {
                close(reader, null, writer, null);
            }
        } else {
            throw new IOException("DataNode does not have index " + indexName);
        }
    }
