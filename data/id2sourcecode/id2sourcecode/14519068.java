    @Override
    public synchronized int index(final IndexPolicy policy, final Iterator<List<Document>> docsIt, final String secondaryId, final boolean deleteExisting) {
        if (policy == null) {
            throw new NullPointerException("index policy not specified");
        }
        IndexWriter writer = null;
        IndexReader reader = null;
        IndexSearcher searcher = null;
        Analyzer analyzer = null;
        final Timer timer = Metric.newTimer("IndexerImpl.index");
        int succeeded = 0;
        int count = 0;
        try {
            Tuple tuple = open(policy);
            writer = tuple.first();
            reader = tuple.second();
            searcher = tuple.third();
            analyzer = writer.getAnalyzer();
            while (docsIt.hasNext()) {
                List<Document> docs = docsIt.next();
                for (Document doc : docs) {
                    try {
                        index(count++, writer, policy, doc, secondaryId, searcher, deleteExisting);
                        succeeded++;
                        if (succeeded % 1000 == 0) {
                            timer.lapse("--succeeded indexing " + succeeded + " documents with analyzer " + analyzer);
                        }
                    } catch (final Exception e) {
                        LOGGER.error("Error indexing " + doc, e);
                    }
                }
            }
        } catch (final Exception e) {
            LOGGER.error("Error indexing documents", e);
        } finally {
            timer.stop("succeeded indexing " + succeeded + "/" + count + " documents with analyzer " + analyzer);
            close(writer, reader, searcher);
            try {
                if (policy.isAddToDictionary()) {
                    SimilarityHelper.getInstance().saveTrainingSpellChecker(indexName);
                }
            } catch (Exception e) {
                LOGGER.error("failed to add spellings", e);
            }
            CacheFlusher.getInstance().flushCaches();
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("#### Indexed " + succeeded + "/" + count + " with analyzer " + analyzer);
        }
        return succeeded;
    }
