    public void getWriteLock() {
        synchronized (mutex) {
            waitingWriters++;
            try {
                while (givenLocks != 0) {
                    if (TRACE) System.out.println(Thread.currentThread().toString() + "waiting for writelock");
                    mutex.wait();
                }
            } catch (java.lang.InterruptedException e) {
                System.out.println(e);
            }
            waitingWriters--;
            givenLocks = -1;
            if (TRACE) System.out.println(Thread.currentThread().toString() + " got writelock, GivenLocks = " + givenLocks);
        }
    }
