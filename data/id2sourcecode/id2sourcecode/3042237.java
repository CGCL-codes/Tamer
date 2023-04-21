    public void run() {
        int config = info.getChannels() == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO : AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        int len = AudioTrack.getMinBufferSize(ASAP.SAMPLE_RATE, config, AudioFormat.ENCODING_PCM_8BIT);
        if (len < 16384) len = 16384;
        final byte[] buffer = new byte[len];
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, ASAP.SAMPLE_RATE, config, AudioFormat.ENCODING_PCM_8BIT, len, AudioTrack.MODE_STREAM);
        audioTrack.play();
        for (; ; ) {
            synchronized (this) {
                if (len < buffer.length || isPaused()) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                    }
                }
                if (stop) {
                    audioTrack.stop();
                    return;
                }
            }
            synchronized (asap) {
                len = asap.generate(buffer, buffer.length, ASAPSampleFormat.U8);
            }
            audioTrack.write(buffer, 0, len);
        }
    }
