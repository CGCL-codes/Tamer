    public int processAudio(AudioBuffer buffer) {
        boolean bypassed = multiBandControls.isBypassed();
        if (bypassed) {
            if (!wasBypassed) {
                clear();
                wasBypassed = true;
            }
            return AUDIO_OK;
        }
        conformBandBuffers(buffer);
        split(midXO, buffer, bandBuffers[0], bandBuffers[1]);
        if (nbands > 2) {
            split(hiXO, bandBuffers[1], bandBuffers[2], bandBuffers[3]);
            split(loXO, bandBuffers[0], bandBuffers[0], bandBuffers[1]);
        }
        for (int b = 0; b < nbands; b++) {
            compressors[b].processAudio(bandBuffers[b]);
        }
        buffer.makeSilence();
        int nc = buffer.getChannelCount();
        int ns = buffer.getSampleCount();
        float out;
        for (int c = 0; c < nc; c++) {
            float[] samples = buffer.getChannel(c);
            for (int b = 0; b < nbands; b++) {
                float[] bandsamples = bandBuffers[b].getChannel(c);
                for (int i = 0; i < ns; i++) {
                    out = bandsamples[i];
                    if (isDenormalOrZero(out)) continue;
                    samples[i] += ((b & 1) == 1) ? -out : out;
                }
            }
        }
        wasBypassed = bypassed;
        return AUDIO_OK;
    }
