    public void stopSource(AudioNode src) {
        synchronized (threadLock) {
            while (!threadLock.get()) {
                try {
                    threadLock.wait();
                } catch (InterruptedException ex) {
                }
            }
            if (audioDisabled) return;
            if (src.getStatus() != Status.Stopped) {
                int chan = src.getChannel();
                assert chan != -1;
                src.setStatus(Status.Stopped);
                src.setChannel(-1);
                clearChannel(chan);
                freeChannel(chan);
                if (src.getAudioData() instanceof AudioStream) {
                    AudioStream stream = (AudioStream) src.getAudioData();
                    if (stream.isOpen()) {
                        stream.close();
                    }
                    deleteAudioData(src.getAudioData());
                }
            }
        }
    }
