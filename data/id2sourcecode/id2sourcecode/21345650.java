    public void commit() {
        if (!isReadOnly()) {
            for (StmResource resource : writeSet) {
                resource.onLockingWriteSet(this);
            }
            final long newClock = incrementGlobalVersionClock();
            if (readVersion + 1 == newClock) {
            } else {
                for (StmResource resource : readSet) {
                    resource.onValidateReadSet(this);
                }
            }
            doActionEnsureAllInvoked(new ResourceAction<StmResource>() {

                public void doAction(StmResource resource) {
                    resource.onCommit(TransactionImpl.this, newClock);
                    resource.onReleaseLocks(TransactionImpl.this);
                }
            }, CommitFailedException.class, writeSet.iterator());
        }
        doActionEnsureAllInvoked(new ResourceAction<StmResource>() {

            public void doAction(StmResource resource) {
                resource.onFinished(TransactionImpl.this, false);
            }
        }, RollbackFailedException.class, new LinkedIterator<StmResource>(writeSet.iterator(), readSet.iterator()));
        executeActions();
    }
