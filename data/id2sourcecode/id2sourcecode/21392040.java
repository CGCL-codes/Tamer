        public void run() {
            runnable.run();
            lock.lock();
            try {
                runningChannels.remove(getChannel());
                hasAvailableRunnable.signal();
            } finally {
                lock.unlock();
            }
        }
