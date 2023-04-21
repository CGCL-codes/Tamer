    public void merge(IndexWriter writer) throws CorruptIndexException, IOException {
        this.writer = writer;
        initMergeThreadPriority();
        dir = writer.getDirectory();
        message("now merge");
        message("  index: " + writer.segString());
        while (true) {
            MergePolicy.OneMerge merge = writer.getNextMerge();
            if (merge == null) {
                message("  no more merges pending; now return");
                return;
            }
            writer.mergeInit(merge);
            synchronized (this) {
                while (mergeThreadCount() >= maxThreadCount) {
                    message("    too many merge threads running; stalling...");
                    try {
                        wait();
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
                message("  consider merge " + merge.segString(dir));
                assert mergeThreadCount() < maxThreadCount;
                final MergeThread merger = getMergeThread(writer, merge);
                mergeThreads.add(merger);
                message("    launch new thread [" + merger.getName() + "]");
                merger.start();
            }
        }
    }
