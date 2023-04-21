    public void pauseSource(AudioNode src) {
        if (audioDisabled) {
            return;
        }
        MediaPlayer mp = musicPlaying.get(src);
        if (mp != null) {
            mp.pause();
            src.setStatus(Status.Paused);
        } else {
            int channel = src.getChannel();
            if (channel != -1) {
                soundPool.pause(channel);
            }
        }
    }
