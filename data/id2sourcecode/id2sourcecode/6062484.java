    protected void processByWaiting(Buffer buffer) {
        synchronized (this) {
            if (!started) {
                prefetched = true;
                return;
            }
        }
        javax.media.format.AudioFormat format = (javax.media.format.AudioFormat) buffer.getFormat();
        int sampleRate = (int) format.getSampleRate();
        int sampleSize = format.getSampleSizeInBits();
        int channels = format.getChannels();
        int timeToWait;
        long duration;
        duration = buffer.getLength() * 1000 / ((sampleSize / 8) * sampleRate * channels);
        timeToWait = (int) (duration / getRate());
        try {
            Thread.sleep(timeToWait);
        } catch (Exception e) {
        }
        buffer.setLength(0);
        buffer.setOffset(0);
        mediaTimeAnchor += duration * 1000000;
    }
