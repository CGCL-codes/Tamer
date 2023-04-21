    public IndexSearcher get(String name) {
        IndexSearcher searcher = null;
        if (cache.containsKey(name)) {
            searcher = cache.get(name);
        } else {
            synchronized (this) {
                if (cache.containsKey(name)) {
                    searcher = cache.get(name);
                } else {
                    IndexWriter writer = IndexWriterCache.getInstance().get(name);
                    IndexReader reader = null;
                    try {
                        reader = IndexReader.open(writer, true);
                    } catch (CorruptIndexException ex) {
                        logger.error("Something is wrong when open lucene IndexWriter", ex);
                    } catch (IOException ex) {
                        logger.error("Something is wrong when open lucene IndexWriter", ex);
                    }
                    searcher = new IndexSearcher(reader);
                    cache.put(name, searcher);
                }
            }
        }
        return searcher;
    }
