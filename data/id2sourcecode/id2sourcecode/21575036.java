    public int processAudio(AudioBuffer buffer) {
        float[] left;
        float[] right;
        if (byteBuffer == null || byteBuffer.length != buffer.getSampleCount() * 2 * nChannel) byteBuffer = new byte[buffer.getSampleCount() * 2 * nChannel];
        int nSamp = buffer.getSampleCount();
        int count = 0;
        if (nChannel == 2) {
            left = buffer.getChannel(0);
            right = buffer.getChannel(1);
            for (int n = 0; n < nSamp; n++) {
                short leftI = (short) (left[n] * 32768f);
                short rightI = (short) (right[n] * 32768f);
                byteBuffer[count++] = (byte) (0xff & leftI);
                byteBuffer[count++] = (byte) (0xff & (leftI >> 8));
                byteBuffer[count++] = (byte) (0xff & rightI);
                byteBuffer[count++] = (byte) (0xff & (rightI >> 8));
            }
        } else {
            left = buffer.getChannel(0);
            for (int n = 0; n < nSamp; n++) {
                short leftI = (short) (left[n] * 32768f);
                byteBuffer[count++] = (byte) (0xff & leftI);
                byteBuffer[count++] = (byte) (0xff & (leftI >> 8));
            }
        }
        try {
            write(byteBuffer, 0, count);
        } catch (IOException e) {
            e.printStackTrace();
            return AudioProcess.AUDIO_DISCONNECT;
        }
        return AUDIO_OK;
    }
