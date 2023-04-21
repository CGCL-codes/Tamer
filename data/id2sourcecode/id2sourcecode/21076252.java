    private Collection writeData(ZipOutputStream zipOut) throws IOException {
        zipOut.putNextEntry(new ZipEntry(DATA_FILE_NAME));
        ExportedDataValueIterator baseIter = new ExportedDataValueIterator(ctx.getData(), ctx.getHierarchy(), filter, metricsIncludes, metricsExcludes);
        DefaultDataExportFilter ddef;
        TaskListDataWatcher taskListWatcher;
        if (baseIter.isUsingExplicitNames()) {
            logger.fine("Using explicit name approach");
            Iterator taskListSearcher = new ExportedDataValueIterator(ctx.getData(), ctx.getHierarchy(), filter, null, Collections.singleton("."));
            taskListWatcher = new TaskListDataWatcher(taskListSearcher);
            while (taskListWatcher.hasNext()) {
                ThreadThrottler.tick();
                taskListWatcher.next();
            }
            ddef = new DefaultDataExportFilter(baseIter);
            ddef.setSkipProcessAutoData(false);
            ddef.setSkipToDateData(false);
            ddef.setSkipNodesAndLeaves(false);
            ddef.init();
        } else {
            logger.fine("Using pattern-based name approach");
            taskListWatcher = new TaskListDataWatcher(baseIter);
            ddef = new DefaultDataExportFilter(taskListWatcher);
            ddef.setIncludes(metricsIncludes);
            ddef.setExcludes(metricsExcludes);
            ddef.init();
        }
        DataExporter exp = new DataExporterXMLv1();
        exp.export(zipOut, ddef);
        baseIter.iterationFinished();
        zipOut.closeEntry();
        return taskListWatcher.getTaskListNames();
    }
