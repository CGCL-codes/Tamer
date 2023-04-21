    public void getWriteLock() {
        synchronized (this.mutex) {
            this.waitingWriters++;
            try {
                while (this.givenLocks != 0) {
                    if (ReadWriteLock.TRACE) {
                        System.out.println(Thread.currentThread().toString() + "waiting for writelock");
                    }
                    this.mutex.wait();
                }
            } catch (java.lang.InterruptedException e) {
                System.out.println(e);
            }
            this.waitingWriters--;
            this.givenLocks = -1;
            if (ReadWriteLock.TRACE) {
                System.out.println(Thread.currentThread().toString() + " got writelock, GivenLocks = " + this.givenLocks);
            }
        }
    }
