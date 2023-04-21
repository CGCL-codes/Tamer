    public void updateInThread(float tpf) {
        if (audioDisabled) return;
        for (int i = 0; i < channels.length; i++) {
            AudioNode src = chanSrcs[i];
            if (src == null) continue;
            int sourceId = channels[i];
            boolean boundSource = i == src.getChannel();
            boolean streaming = src.getAudioData() instanceof AudioStream;
            assert (boundSource && streaming) || (!streaming);
            int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
            boolean wantPlaying = src.getStatus() == Status.Playing;
            boolean stopped = state == AL_STOPPED;
            if (streaming && wantPlaying) {
                AudioStream stream = (AudioStream) src.getAudioData();
                if (stream.isOpen()) {
                    fillStreamingSource(sourceId, stream);
                    if (stopped) alSourcePlay(sourceId);
                } else {
                    if (stopped) {
                        src.setStatus(Status.Stopped);
                        src.setChannel(-1);
                        clearChannel(i);
                        freeChannel(i);
                        deleteAudioData(stream);
                    }
                }
            } else if (!streaming) {
                boolean paused = state == AL_PAUSED;
                assert (src.getStatus() == Status.Paused && paused) || (!paused);
                if (stopped) {
                    if (boundSource) {
                        src.setStatus(Status.Stopped);
                        src.setChannel(-1);
                    }
                    clearChannel(i);
                    freeChannel(i);
                }
            }
        }
        objManager.deleteUnused(this);
    }
