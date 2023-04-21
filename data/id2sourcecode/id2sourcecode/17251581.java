    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("=============================== MOJO PARAMETERS ================================");
        System.out.println("parameter 'solrServerUrl' = " + solrServerUrl);
        System.out.println("parameter 'ontologyMappings' = " + ontologyMappings);
        System.out.println("parameter 'recreateOntologyData' = " + recreateOntologyData);
        System.out.println("parameter 'mitabFileUrl' = " + mitabFileUrl);
        System.out.println("parameter 'hasMitabHeader' = " + hasMitabHeader);
        System.out.println("parameter 'removeExistingData' = " + removeExistingData);
        System.out.println("parameter 'firstLine' = " + firstLine);
        System.out.println("parameter 'maxLines' = " + maxLines);
        System.out.println("parameter 'logFilePath' = " + logFilePath);
        System.out.println("================================================================================");
        if (logFilePath != null) {
            File logFile = new File(logFilePath);
            try {
                logWriter = new BufferedWriter(new FileWriter(logFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        URL mitabFile = null;
        try {
            mitabFile = new URL(mitabFileUrl);
        } catch (MalformedURLException e) {
            throw new MojoExecutionException("Could not parse the MITAB url: " + mitabFileUrl, e);
        }
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        writeLog("Available processors: " + availableProcessors);
        try {
            if (removeExistingData) {
                writeLog("Removing existing data...");
                SolrServer server = new CommonsHttpSolrServer(solrServerUrl);
                server.deleteByQuery("*:*");
                server.commit();
                server.optimize();
            }
        } catch (Exception e) {
            throw new MojoExecutionException("An error occur while removing existing data", e);
        }
        try {
            if (recreateOntologyData) {
                writeLog("Re-creating ontology data...");
                SolrServer ontologyServer = new CommonsHttpSolrServer(ontologySolrServerUrl);
                ontologyServer.deleteByQuery("*:*");
                ontologyServer.commit();
                ontologyServer.optimize();
                OntologyIndexer indexer = new OntologyIndexer(ontologyServer);
                indexer.indexObo(ontologyMappings.toArray(new uk.ac.ebi.intact.plugins.OntologyMapping[ontologyMappings.size()]));
            }
        } catch (Exception e) {
            throw new MojoExecutionException("An error occur while rebuilding ontologies", e);
        }
        int interactionsCount;
        try {
            interactionsCount = (maxLines != null) ? maxLines : countLines(mitabFile, hasMitabHeader);
        } catch (IOException e) {
            throw new MojoExecutionException("An error occur while counting the MITAB lines from " + mitabFile.getFile(), e);
        }
        writeLog("Interactions to process: " + interactionsCount);
        final int defaultBatchSize = interactionsCount / availableProcessors;
        final int originalFirstLine = firstLine;
        Collection<IndexerWorker> workers = new ArrayList<IndexerWorker>(availableProcessors);
        for (int i = 0; i < availableProcessors; i++) {
            final int remainingLines = originalFirstLine + interactionsCount - firstLine;
            final boolean isLastProcessor = (i == availableProcessors - 1);
            final int batchSize = isLastProcessor ? remainingLines : defaultBatchSize;
            final String threadName = "MITAB-processor-" + i;
            writeLog("Starting thread '" + threadName + "' to process lines " + firstLine + ".." + (firstLine + batchSize) + " (batchSize=" + batchSize + ") ...");
            final IndexerWorker worker = new IndexerWorker(mitabFile, hasMitabHeader, solrServerUrl, ontologySolrServerUrl, firstLine, batchSize);
            workers.add(worker);
            Thread thread = new Thread(worker, threadName);
            thread.start();
            firstLine = firstLine + batchSize;
        }
        writeLog("All processors have received a chunk of data to process.");
        Thread thread = new Thread(new DocCounter(solrServerUrl, interactionsCount), "DocCounter");
        thread.start();
        do {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Iterator<IndexerWorker> iterator = workers.iterator(); iterator.hasNext(); ) {
                IndexerWorker worker = iterator.next();
                if (worker.isDone()) {
                    iterator.remove();
                }
            }
        } while (!workers.isEmpty());
    }
