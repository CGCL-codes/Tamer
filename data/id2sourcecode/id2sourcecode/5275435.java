    public void run() {
        while (!isInterrupted()) {
            threadLock.writeLock().lock();
            usrLock.writeLock().lock();
            avgInfo = info;
            info = new Info();
            threadLock.writeLock().unlock();
            usrLock.writeLock().unlock();
            try {
                sleep(interval);
            } catch (InterruptedException ex) {
                interrupt();
            }
        }
    }
