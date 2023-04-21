    public void rollback() {
        try {
            if (!readOnly) {
                doActionEnsureAllInvoked(new ResourceAction<StmResource>() {

                    public void doAction(StmResource resource) {
                        resource.onReleaseLocks(TransactionImpl.this);
                    }
                }, RollbackFailedException.class, writeSet.iterator());
            }
        } finally {
            doActionEnsureAllInvoked(new ResourceAction<StmResource>() {

                public void doAction(StmResource resource) {
                    resource.onFinished(TransactionImpl.this, true);
                }
            }, RollbackFailedException.class, new LinkedIterator<StmResource>(writeSet.iterator(), readSet.iterator()));
        }
    }
