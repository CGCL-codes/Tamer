    private void restoreInvariantUpgraderCancelled() {
        if (this.readersWaitingCount > 0 && this.writersWaitingCount == 0) {
            this.readersWaiting.signalAll();
        }
    }
