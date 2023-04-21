    public void stopSource(AudioNode src) {
        if (audioDisabled) {
            return;
        }
        MediaPlayer mp = musicPlaying.get(src);
        if (mp != null) {
            mp.stop();
            mp.reset();
            src.setStatus(Status.Stopped);
        } else {
            int channel = src.getChannel();
            if (channel != -1) {
                soundPool.pause(channel);
            }
        }
    }
