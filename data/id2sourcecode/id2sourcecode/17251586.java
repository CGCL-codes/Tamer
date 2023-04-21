        public void run() {
            final String name = Thread.currentThread().getName();
            writeLog(name + " - Thread started - firstLine:" + firstLine + " - batchSize:" + batchSize);
            try {
                SolrServer solrServer = new CommonsHttpSolrServer(solrServerUrl);
                SolrServer ontologyServer = new CommonsHttpSolrServer(ontologyServerUrl);
                final long startTime = System.currentTimeMillis();
                IntactSolrIndexer indexer = new IntactSolrIndexer(solrServer, ontologyServer);
                indexer.indexMitab(mitabFile.openStream(), hasHeader, firstLine, batchSize);
                final long elapsedTime = System.currentTimeMillis() - startTime;
                final long linePerSeconds = batchSize / (elapsedTime / 1000);
                writeLog(name + " - Completed chunk - Elapsed time :" + elapsedTime + "ms - " + linePerSeconds + " lines/sec.");
                isDone = true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(name + " - Problem processing mitab file - firstLine=" + firstLine + " - batchSize=" + batchSize, e);
            }
        }
