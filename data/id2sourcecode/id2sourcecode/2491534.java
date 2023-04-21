    public boolean tryExclusiveLock() {
        boolean result;
        this.lock.lock();
        try {
            if (writers == 0 && readers == 0) {
                writers++;
                result = true;
            } else {
                result = false;
            }
        } finally {
            this.lock.unlock();
        }
        return result;
    }
