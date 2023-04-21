    private void changeAmplitude(int start, int end, float delta, boolean divide) {
        float change = 1.0f + delta;
        if (floatBuffer == null) floatBuffer = new FloatSampleBuffer();
        int byteStart = start * audioFormat.getFrameSize();
        int byteEnd = end * audioFormat.getFrameSize();
        floatBuffer.initFromByteArray(streamData.get(), byteStart, byteEnd - byteStart, audioFormat);
        for (int nChannel = 0; nChannel < floatBuffer.getChannelCount(); nChannel++) {
            float afBuffer[] = floatBuffer.getChannel(nChannel);
            for (int nSample = 0; nSample < floatBuffer.getSampleCount(); nSample++) {
                if (divide) afBuffer[nSample] /= change; else afBuffer[nSample] *= change;
            }
        }
        floatBuffer.convertToByteArray(streamData.get(), byteStart, audioFormat);
    }
