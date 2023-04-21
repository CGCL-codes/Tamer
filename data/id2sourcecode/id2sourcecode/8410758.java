    protected AudioFormat getDefaultTargetFormat(AudioFormat targetFormat, AudioFormat sourceFormat, boolean allowNotSpecified) {
        float targetSampleRate = targetFormat.getSampleRate();
        if (targetSampleRate == AudioSystem.NOT_SPECIFIED) {
            targetSampleRate = sourceFormat.getSampleRate();
        }
        if ((!allowNotSpecified && targetSampleRate == AudioSystem.NOT_SPECIFIED) || (targetSampleRate != AudioSystem.NOT_SPECIFIED && sourceFormat.getSampleRate() != AudioSystem.NOT_SPECIFIED && targetSampleRate != sourceFormat.getSampleRate())) {
            throw new IllegalArgumentException("Illegal sample rate (" + targetSampleRate + ") !");
        }
        int targetChannels = targetFormat.getChannels();
        if (targetChannels == AudioSystem.NOT_SPECIFIED) {
            targetChannels = sourceFormat.getChannels();
        }
        if ((!allowNotSpecified && targetChannels == AudioSystem.NOT_SPECIFIED) || (targetChannels != AudioSystem.NOT_SPECIFIED && sourceFormat.getChannels() != AudioSystem.NOT_SPECIFIED && targetChannels != sourceFormat.getChannels())) {
            throw new IllegalArgumentException("Illegal number of channels (" + targetChannels + ") !");
        }
        AudioFormat newTargetFormat = new AudioFormat(targetFormat.getEncoding(), targetSampleRate, MPEG_BITS_PER_SAMPLE, targetChannels, getFrameSize(targetFormat.getEncoding(), targetSampleRate, MPEG_BITS_PER_SAMPLE, targetChannels, MPEG_FRAME_RATE, false, 0), MPEG_FRAME_RATE, false);
        return newTargetFormat;
    }
