    private void initConverter(AudioFormat inFormat) {
        lastFormat = inFormat;
        numberOfInputChannels = inFormat.getChannels();
        if (outputFormat != null) {
            numberOfOutputChannels = outputFormat.getChannels();
        }
        inputSampleSize = inFormat.getSampleSizeInBits();
        bigEndian = inFormat.getEndian() == AudioFormat.BIG_ENDIAN;
        if ((numberOfInputChannels == 2) && (numberOfOutputChannels == 1)) {
            downmix = true;
        } else {
            downmix = false;
        }
    }
