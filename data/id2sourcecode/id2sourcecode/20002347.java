    protected void notifyOfDeltas(ImmutableList<WaveletDeltaRecord> deltas, ImmutableSet<String> domainsToNotify) {
        Preconditions.checkState(writeLock.isHeldByCurrentThread(), "must hold write lock");
        Preconditions.checkArgument(!deltas.isEmpty(), "empty deltas");
        HashedVersion endVersion = deltas.get(deltas.size() - 1).getResultingVersion();
        HashedVersion currentVersion = getCurrentVersion();
        Preconditions.checkArgument(endVersion.equals(currentVersion), "cannot notify of deltas ending in %s != current version %s", endVersion, currentVersion);
        notifiee.waveletUpdate(waveletState.getSnapshot(), deltas, domainsToNotify);
    }
