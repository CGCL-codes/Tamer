    @Override
    public void processAudioSynchronized(AudioBuffer buffer) {
        if (!running) return;
        boolean realTime = buffer.isRealTime();
        if (byteBuffer == null || byteBuffer.length != buffer.getSampleCount() * 2 * nChannel) byteBuffer = new byte[buffer.getSampleCount() * 2 * nChannel];
        long seekPos = getFramePos();
        if (seekPos != framePos) {
            try {
                ais.seekFrame(seekPos, realTime);
                framePos = seekPos;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        ais.processAudio(buffer);
        if (ais.getChannels() == 1) {
            buffer.copyChannel(0, 1);
        }
        framePos += buffer.getSampleCount();
    }
