    public void initFromByteArray(byte[] buffer, int offset, int byteCount, AudioFormat format, boolean lazy) {
        if (offset + byteCount > buffer.length) {
            throw new IllegalArgumentException("FloatSampleBuffer.initFromByteArray: buffer too small.");
        }
        boolean signed = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
        if (!signed && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            throw new IllegalArgumentException("FloatSampleBuffer: only PCM samples are possible.");
        }
        int bytesPerSample = format.getSampleSizeInBits() / 8;
        int bytesPerFrame = bytesPerSample * format.getChannels();
        int thisSampleCount = byteCount / bytesPerFrame;
        init(format.getChannels(), thisSampleCount, format.getSampleRate(), lazy);
        int formatType = getFormatType(format.getSampleSizeInBits(), signed, format.isBigEndian());
        originalFormatType = formatType;
        for (int ch = 0; ch < format.getChannels(); ch++) {
            convertByteToFloat(buffer, offset, bytesPerFrame, formatType, getChannel(ch), 0, sampleCount);
            offset += bytesPerSample;
        }
    }
