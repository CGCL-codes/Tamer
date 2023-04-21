    public WaveletContainerImpl(WaveletName waveletName, WaveletNotificationSubscriber notifiee, final ListenableFuture<? extends WaveletState> waveletStateFuture, String waveDomain) {
        this.waveletName = waveletName;
        this.notifiee = notifiee;
        this.sharedDomainParticipantId = waveDomain != null ? ParticipantIdUtil.makeUnsafeSharedDomainParticipantId(waveDomain) : null;
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
        waveletStateFuture.addListener(new Runnable() {

            @Override
            public void run() {
                acquireWriteLock();
                try {
                    Preconditions.checkState(waveletState == null, "Repeat attempts to set wavelet state");
                    Preconditions.checkState(state == State.LOADING, "Unexpected state %s", state);
                    waveletState = FutureUtil.getResultOrPropagateException(waveletStateFuture, PersistenceException.class);
                    Preconditions.checkState(waveletState.getWaveletName().equals(getWaveletName()), "Wrong wavelet state, named %s, expected %s", waveletState.getWaveletName(), getWaveletName());
                    state = State.OK;
                } catch (PersistenceException e) {
                    LOG.warning("Failed to load wavelet " + getWaveletName(), e);
                    state = State.CORRUPTED;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOG.warning("Interrupted loading wavelet " + getWaveletName(), e);
                    state = State.CORRUPTED;
                } catch (RuntimeException e) {
                    LOG.severe("Unexpected exception loading wavelet " + getWaveletName(), e);
                    state = State.CORRUPTED;
                } finally {
                    releaseWriteLock();
                }
                loadLatch.countDown();
            }
        }, storageContinuationExecutor);
    }
