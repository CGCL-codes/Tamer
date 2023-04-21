    public void getExclusiveLock(long maxWaitTime) throws InterruptedException, ExceededWaitTimeException, Exception {
        boolean waitedLessThanWaitTime = true;
        this.lock.lock();
        try {
            writersWaitingCount++;
            try {
                try {
                    if ((readers > 0 || writers > 0 || writersWaitingCount > 1) && waitedLessThanWaitTime) {
                        waitedLessThanWaitTime = this.writersWaiting.await(maxWaitTime, TimeUnit.MILLISECONDS);
                    }
                    while ((readers > 0 || writers > 0) && waitedLessThanWaitTime) {
                        waitedLessThanWaitTime = this.writersWaiting.await(maxWaitTime, TimeUnit.MILLISECONDS);
                    }
                } catch (InterruptedException e) {
                    this.restoreInvariantWriterCancelled();
                    throw e;
                } catch (Exception e) {
                    this.restoreInvariantWriterCancelled();
                    throw e;
                }
            } finally {
                writersWaitingCount--;
            }
            if (readers > 0 || writers > 0) {
                this.restoreInvariantWriterCancelled();
                throw new ExceededWaitTimeException("Max time to wait was: " + maxWaitTime);
            }
            writers++;
        } finally {
            this.lock.unlock();
        }
    }
