    PerDPResources(WorkerBuildContext context, DirectoryProvider<?> dp) {
        DirectoryProviderData directoryProviderData = context.getDirectoryProviderData(dp);
        errorHandler = context.getErrorHandler();
        workspace = new Workspace(context, dp, errorHandler);
        visitor = new LuceneWorkVisitor(workspace, context);
        int maxQueueLength = directoryProviderData.getMaxQueueLength();
        executor = Executors.newFixedThreadPool(1, "Directory writer", maxQueueLength);
        exclusiveIndexUsage = directoryProviderData.isExclusiveIndexUsage();
    }
