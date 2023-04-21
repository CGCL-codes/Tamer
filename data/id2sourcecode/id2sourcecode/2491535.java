    public boolean tryUpgradeLock() {
        this.lock.lock();
        try {
            if (readers == 0) {
                this.throwError("There should already be a reader (the same thread) if it is trying to upgrade");
            }
            if (writers == 0 && readers == 1) {
                readers--;
                writers++;
                return true;
            } else {
                return false;
            }
        } finally {
            this.lock.unlock();
        }
    }
