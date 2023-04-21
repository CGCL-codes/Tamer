    public int convertToByteArray(byte[] buffer, int offset, AudioFormat format) {
        int byteCount = getByteArrayBufferSize(format);
        if (offset + byteCount > buffer.length) {
            throw new IllegalArgumentException("FloatSampleBuffer.convertToByteArray: buffer too small.");
        }
        boolean signed = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
        if (!signed && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            throw new IllegalArgumentException("FloatSampleBuffer.convertToByteArray: only PCM samples are allowed.");
        }
        if (format.getSampleRate() != getSampleRate()) {
            throw new IllegalArgumentException("FloatSampleBuffer.convertToByteArray: different samplerates.");
        }
        if (format.getChannels() != getChannelCount()) {
            throw new IllegalArgumentException("FloatSampleBuffer.convertToByteArray: different channel count.");
        }
        int bytesPerSample = format.getSampleSizeInBits() / 8;
        int bytesPerFrame = bytesPerSample * format.getChannels();
        int formatType = getFormatType(format.getSampleSizeInBits(), signed, format.isBigEndian());
        for (int ch = 0; ch < format.getChannels(); ch++) {
            convertFloatToByte(getChannel(ch), sampleCount, buffer, offset, bytesPerFrame, formatType);
            offset += bytesPerSample;
        }
        return getSampleCount() * bytesPerFrame;
    }
