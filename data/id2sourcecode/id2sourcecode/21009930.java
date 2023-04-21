    public IndexingQueue(@NotNull final IndexRegistry indexRegistry, int reporterCapacity) {
        this.indexRegistry = indexRegistry;
        this.reporterCapacity = reporterCapacity;
        readLock = indexRegistry.getReadLock();
        writeLock = indexRegistry.getWriteLock();
        readyTaskAvailable = writeLock.newCondition();
        evtRemoved.add(new Event.Listener<Task>() {

            public void update(Task task) {
                boolean isQueueEmpty;
                LuceneIndex luceneIndex = task.getLuceneIndex();
                writeLock.lock();
                try {
                    boolean isRebuild = task.is(IndexAction.REBUILD);
                    boolean notStartedYet = task.is(TaskState.NOT_READY) || task.is(TaskState.READY);
                    if (isRebuild && notStartedYet) {
                        assert !indexRegistry.getIndexes().contains(luceneIndex);
                        indexRegistry.addIndex(luceneIndex);
                    }
                    isQueueEmpty = tasks.isEmpty();
                } finally {
                    writeLock.unlock();
                }
                if (isQueueEmpty) evtQueueEmpty.fire(null);
            }
        });
        thread = new Thread(IndexingQueue.class.getName()) {

            public void run() {
                while (threadLoop()) ;
                evtWorkerThreadTerminated.fire(null);
            }
        };
        thread.start();
    }
