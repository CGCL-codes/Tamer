    public void read(FloatSampleBuffer buffer, int offset, int sampleCount) {
        if (sampleCount == 0 || isDone()) {
            buffer.setSampleCount(offset, true);
            return;
        }
        if (buffer.getChannelCount() != getChannels()) {
            throw new IllegalArgumentException("read: passed buffer has different channel count");
        }
        if (sourceInput != null) {
            sourceInput.read(buffer, offset, sampleCount);
        } else {
            int byteBufferSize = buffer.getSampleCount() * getFormat().getFrameSize();
            byte[] lTempBuffer = tempBuffer;
            if (lTempBuffer == null || byteBufferSize > lTempBuffer.length) {
                lTempBuffer = new byte[byteBufferSize];
                tempBuffer = lTempBuffer;
            }
            int readSamples = 0;
            int byteOffset = 0;
            while (readSamples < sampleCount) {
                int readBytes;
                try {
                    readBytes = sourceStream.read(lTempBuffer, byteOffset, byteBufferSize);
                } catch (IOException ioe) {
                    readBytes = -1;
                }
                if (readBytes < 0) {
                    eofReached = true;
                    readBytes = 0;
                    break;
                } else if (readBytes == 0) {
                    Thread.yield();
                } else {
                    readSamples += readBytes / getFormat().getFrameSize();
                    byteBufferSize -= readBytes;
                    byteOffset += readBytes;
                }
            }
            buffer.setSampleCount(offset + readSamples, (offset > 0));
            if (readSamples > 0) {
                buffer.setSamplesFromBytes(lTempBuffer, 0, getFormat(), offset, readSamples);
            }
        }
    }
