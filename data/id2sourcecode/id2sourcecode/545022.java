            public void run() {
                try {
                    threadAssertFalse(lock.writeLock().tryLock(1, TimeUnit.MILLISECONDS));
                } catch (Exception ex) {
                    threadUnexpectedException();
                }
            }
