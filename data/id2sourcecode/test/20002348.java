    protected void notifyOfCommit(HashedVersion version, ImmutableSet<String> domainsToNotify) {
        Preconditions.checkState(writeLock.isHeldByCurrentThread(), "must hold write lock");
        notifiee.waveletCommitted(getWaveletName(), version, domainsToNotify);
    }
