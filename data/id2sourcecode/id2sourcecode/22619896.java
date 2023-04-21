    public void pauseSource(AudioNode src) {
        checkDead();
        synchronized (threadLock) {
            while (!threadLock.get()) {
                try {
                    threadLock.wait();
                } catch (InterruptedException ex) {
                }
            }
            if (audioDisabled) return;
            if (src.getStatus() == Status.Playing) {
                assert src.getChannel() != -1;
                alSourcePause(channels[src.getChannel()]);
                src.setStatus(Status.Paused);
            }
        }
    }
