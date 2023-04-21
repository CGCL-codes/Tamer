        @Override
        public void run() {
            int status = -1;
            try {
                while ((status = channel.getChannelStatus()) != 1 && status != 0) {
                    synchronized (lock) {
                        try {
                            lock.wait(1000 * 60);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (AgiException e) {
            }
            if (SafletEngine.debuggerLog.isDebugEnabled()) SafletEngine.debuggerLog.debug("KeepAlive for channel " + channel.getName() + " stopped...");
        }
