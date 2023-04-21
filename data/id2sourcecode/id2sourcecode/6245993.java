        protected boolean lockClient() {
            Runtime.getRuntime().addShutdownHook(new shutdownThread());
            try {
                setMutex(new RandomAccessFile("Equil.tmp", "rw"));
                setMonitor(getMutex().getChannel());
                try {
                    setLock(getMonitor().tryLock());
                } catch (Exception e) {
                    unLockClient();
                    return false;
                }
                if (getLock() == null) {
                    unLockClient();
                    return false;
                }
                return true;
            } catch (Exception e) {
                unLockClient();
                return false;
            }
        }
