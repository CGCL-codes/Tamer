        @Override
        public int read(byte[] abData, int nOffset, int nLength) throws IOException {
            if (isClosed()) {
                return -1;
            }
            int frameCount = nLength / getFrameSize();
            if (writeBuffer == null) {
                writeBuffer = new FloatSampleBuffer(getFormat().getChannels(), frameCount, getFormat().getSampleRate());
            } else {
                writeBuffer.changeSampleCount(frameCount, false);
            }
            read(writeBuffer);
            if (writeBuffer.getSampleCount() == 0 && eofReached) {
                return -1;
            }
            int written = writeBuffer.convertToByteArray(abData, nOffset, getFormat());
            return written;
        }
