    protected int readBytesFromFloatInput(byte[] abData, int nOffset, int nLength) throws IOException {
        FloatSampleInput lInput = sourceInput;
        if (lInput.isDone()) {
            return -1;
        }
        int frameCount = nLength / getFormat().getFrameSize();
        FloatSampleBuffer lTempBuffer = tempFloatBuffer;
        if (lTempBuffer == null) {
            lTempBuffer = new FloatSampleBuffer(getFormat().getChannels(), frameCount, getFormat().getSampleRate());
            tempFloatBuffer = lTempBuffer;
        } else {
            lTempBuffer.setSampleCount(frameCount, false);
        }
        lInput.read(lTempBuffer);
        if (lInput.isDone()) {
            return -1;
        }
        if (abData != null) {
            int writtenBytes = tempFloatBuffer.convertToByteArray(abData, nOffset, getFormat());
            return writtenBytes;
        }
        return frameCount * getFormat().getFrameSize();
    }
