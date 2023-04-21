    public void put(Runnable o) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (queue.remainingCapacity() <= 0) {
                queueNotFull.await();
            }
            queue.put(o);
            ChannelsRunnable r = (ChannelsRunnable) o;
            if (!runningChannels.contains(r.getChannel())) hasAvailableRunnable.signal();
        } finally {
            lock.unlock();
        }
    }
