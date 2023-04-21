        public IndexerThread(int i, IndexWriter writer) {
            setName("Indexer " + i);
            this.writer = writer;
        }
