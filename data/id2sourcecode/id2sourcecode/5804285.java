        public IndexerThread(IndexWriter writer, TimedThread[] threads) {
            super(threads);
            this.writer = writer;
        }
