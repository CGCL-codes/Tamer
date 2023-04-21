    protected synchronized MergeThread getMergeThread(IndexWriter writer, MergePolicy.OneMerge merge) throws IOException {
        final MergeThread thread = new MergeThread(writer, merge);
        thread.setThreadPriority(mergeThreadPriority);
        thread.setDaemon(true);
        thread.setName("Lucene Merge Thread #" + mergeThreadCount++);
        return thread;
    }
