    public void setSamplesFromBytes(byte[] srcBuffer, int srcOffset, AudioFormat format, int destOffset, int lengthInSamples) {
        int bytesPerSample = (format.getSampleSizeInBits() + 7) / 8;
        int bytesPerFrame = bytesPerSample * format.getChannels();
        if (srcOffset + (lengthInSamples * bytesPerFrame) > srcBuffer.length) {
            throw new IllegalArgumentException("FloatSampleBuffer.setSamplesFromBytes: srcBuffer too small.");
        }
        if (destOffset + lengthInSamples > getSampleCount()) {
            throw new IllegalArgumentException("FloatSampleBuffer.setSamplesFromBytes: destBuffer too small.");
        }
        boolean signed = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
        boolean unsigned = format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED);
        if (!signed && !unsigned) {
            throw new IllegalArgumentException("FloatSampleBuffer: only PCM samples are possible.");
        }
        int formatType = getFormatType(format.getSampleSizeInBits(), signed, format.isBigEndian());
        for (int ch = 0; ch < format.getChannels(); ch++) {
            convertByteToFloat(srcBuffer, srcOffset, bytesPerFrame, formatType, getChannel(ch), destOffset, lengthInSamples);
            srcOffset += bytesPerSample;
        }
    }
