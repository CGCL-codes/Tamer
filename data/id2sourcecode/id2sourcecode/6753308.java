        public final synchronized int getChannelCount() {
            if (channelCount == 0) {
                Object lockToken = null;
                RWLock lock = null;
                String address = container.getAddressAsString();
                SwitchContainer sc = (SwitchContainer) container;
                try {
                    long start = System.currentTimeMillis();
                    lock = getLock();
                    lockToken = lock.getWriteLock();
                    long gotLock = System.currentTimeMillis();
                    getDevicePath(address).open();
                    byte[] state = sc.readDevice();
                    channelCount = sc.getNumberChannels(state);
                    long now = System.currentTimeMillis();
                    logger.info(address + " has " + channelCount + " channel[s], took us " + (now - start) + "ms to figure out (" + (gotLock - start) + " to get the lock, " + (now - gotLock) + " to retrieve)");
                } catch (Throwable t) {
                    logger.warn(address + ": can't retrieve channel count (assuming 2):", t);
                    channelCount = 2;
                } finally {
                    if (lock != null) {
                        lock.release(lockToken);
                    }
                }
            }
            return channelCount;
        }
