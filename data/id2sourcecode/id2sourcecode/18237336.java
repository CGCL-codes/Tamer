    public void setFormat(AudioFormat sourceFormat, AudioFormat targetFormat) {
        this.sourceFormat = sourceFormat;
        if (sourceFormat != null) {
            sourceEncoding = sourceFormat.getEncoding();
            sourceSampleSizeInBits = sourceFormat.getSampleSizeInBits();
            sourceByteOrder = sourceFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
            sourceChannels = sourceFormat.getChannels();
            sourceSampleRate = Math.round(sourceFormat.getSampleRate());
            sourceIsBigEndian = sourceFormat.isBigEndian();
            if (sourceFormat.getSampleRate() < 32000 && bitRate > 160) {
                bitRate = 160;
            }
        }
        int targetSampleRate = -1;
        this.targetFormat = targetFormat;
        if (targetFormat != null) {
            targetSampleRate = Math.round(targetFormat.getSampleRate());
        }
        int result = nInitParams(sourceChannels, sourceSampleRate, targetSampleRate, bitRate, chMode, quality, vbrMode, sourceIsBigEndian);
        if (result < 0) {
            throw new IllegalArgumentException("parameters not supported by LAME (returned " + result + ")");
        }
    }
