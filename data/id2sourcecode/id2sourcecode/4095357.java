    DirectoryProviderWorkspace(WorkerBuildContext context, DirectoryProvider<?> dp, MassIndexerProgressMonitor monitor, int maxThreads, ErrorHandler errorHandler) {
        if (maxThreads < 1) {
            throw new IllegalArgumentException("maxThreads needs to be at least 1");
        }
        this.monitor = monitor;
        workspace = new Workspace(context, dp, errorHandler);
        visitor = new LuceneWorkVisitor(workspace, context);
        executor = Executors.newFixedThreadPool(maxThreads, "indexwriter");
    }
