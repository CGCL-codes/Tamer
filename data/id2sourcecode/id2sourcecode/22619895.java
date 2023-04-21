    public void playSource(AudioNode src) {
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
                return;
            } else if (src.getStatus() == Status.Stopped) {
                int index = newChannel();
                if (index == -1) {
                    logger.log(Level.WARNING, "No channel available to play {0}", src);
                    return;
                }
                clearChannel(index);
                src.setChannel(index);
                AudioData data = src.getAudioData();
                if (data.isUpdateNeeded()) updateAudioData(data);
                chanSrcs[index] = src;
                setSourceParams(channels[index], src, false);
                attachAudioToSource(channels[index], data);
            }
            alSourcePlay(channels[src.getChannel()]);
            src.setStatus(Status.Playing);
        }
    }
