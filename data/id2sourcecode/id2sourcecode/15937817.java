    public void output(short[] leftBuffer, short[] rightBuffer, int length, Resampler resampler, boolean snapBack) {
        Channel channel;
        int sample, leftAmp, rightAmp;
        int numChan = synthesizer.getNumberOfChannels();
        channel = synthesizer.getChannel(0);
        if (channel.isSilent()) {
            for (int n = 0; n < length; n++) {
                leftMixBuffer[n] = 0;
                rightMixBuffer[n] = 0;
            }
        } else {
            leftAmp = (channel.getLeftAmplitude() * gain / numChan) >> 15;
            rightAmp = (channel.getRightAmplitude() * gain / numChan) >> 15;
            channel.getAudio(leftBuffer, length, resampler, snapBack);
            waveScaler.scaleWaves(leftBuffer, leftMixBuffer, length, leftAmp);
            waveScaler.scaleWaves(leftBuffer, rightMixBuffer, length, rightAmp);
        }
        for (int n = 1; n < numChan; n++) {
            channel = synthesizer.getChannel(n);
            if (!channel.isSilent()) {
                leftAmp = (channel.getLeftAmplitude() * gain / numChan) >> 15;
                rightAmp = (channel.getRightAmplitude() * gain / numChan) >> 15;
                channel.getAudio(leftBuffer, length, resampler, snapBack);
                waveScaler.scaleWavesAccumulate(leftBuffer, leftMixBuffer, length, leftAmp);
                waveScaler.scaleWavesAccumulate(leftBuffer, rightMixBuffer, length, rightAmp);
            }
        }
        waveShaper.shapeWaves(leftMixBuffer, leftBuffer, length);
        waveShaper.shapeWaves(rightMixBuffer, rightBuffer, length);
    }
