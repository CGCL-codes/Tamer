    private boolean handleEnqueueable(EnqueuedEnqueueable ee, long now, float[][] outputs, int sampleFrames) {
        Enqueueable e = ee.getEnqueueable();
        long start = ee.getStart();
        long offsetIntoEnqueueable = now - start;
        e.setModulationGlobalOffset(start);
        if (offsetIntoEnqueueable < 0) return true;
        for (int i = 0; i < sampleFrames; i++) {
            long offset = i + offsetIntoEnqueueable;
            for (int channel = 0; channel < e.getChannelCount(); channel++) {
                for (int output : e.getOutputMap().getOutputs(channel)) {
                    float data = e.getAudioData(channel, offset);
                    if (data == Enqueueable.NO_MORE_AUDIO_DATA) return false;
                    outputs[output][i] += data;
                }
            }
        }
        return true;
    }
