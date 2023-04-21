    protected void restoreInvariant() {
        if (readers == 0) {
            if (writers == 0) {
                if (writersWaitingCount > 0) {
                    this.writersWaiting.signal();
                } else if (readersWaitingCount > 0) {
                    this.readersWaiting.signal();
                }
            }
        } else {
            if (readers == 1 && upgradersWaitingCount == 1) {
                upgradersWaiting.signal();
            } else {
                if (readersWaitingCount > 0 && upgradersWaitingCount == 0 && writersWaitingCount == 0) {
                    this.readersWaiting.signal();
                }
            }
        }
    }
