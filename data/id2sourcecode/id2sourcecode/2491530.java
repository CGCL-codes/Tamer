    private void restoreInvariantWriterCancelled() {
        if (this.readersWaitingCount > 0 && this.writersWaitingCount == 0 && this.upgradersWaitingCount == 0) {
            this.readersWaiting.signalAll();
        }
    }
