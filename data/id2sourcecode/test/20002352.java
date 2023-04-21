    protected void persist(final HashedVersion version, final ImmutableSet<String> domainsToNotify) {
        Preconditions.checkState(writeLock.isHeldByCurrentThread(), "must hold write lock");
        final ListenableFuture<Void> result = waveletState.persist(version);
        result.addListener(new Runnable() {

            @Override
            public void run() {
                acquireWriteLock();
                try {
                    notifyOfCommit(version, domainsToNotify);
                } finally {
                    releaseWriteLock();
                }
            }
        }, storageContinuationExecutor);
    }
