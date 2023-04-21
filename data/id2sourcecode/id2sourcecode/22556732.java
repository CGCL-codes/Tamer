    public void playDataChunk(DataChunk samples) {
        int nItems = samples.getSize();
        AudioFormat format = outputLine.getFormat();
        int channelNumber = format.getChannels();
        int sampleSizeInBytes = format.getSampleSizeInBits() / 8;
        boolean bigEndian = format.isBigEndian();
        int domainExtend = (sampleSizeInBytes == 2) ? 256 : 1;
        for (int i = 0; i < nItems; i++) {
            double sample = (samples.getElement(i));
            byte outputBuffer[] = new byte[sampleSizeInBytes * channelNumber];
            for (int channel = 0; channel < channelNumber; channel++) {
                for (int bytes = 0; bytes < sampleSizeInBytes; bytes++) {
                    if (bigEndian) outputBuffer[channel * channelNumber + bytes] = (byte) (((short) (sample * domainExtend) >> (8 * (sampleSizeInBytes - bytes - 1))) & 0xFF); else outputBuffer[channel * channelNumber + bytes] = (byte) (((short) (sample * domainExtend) >> (8 * bytes)) & 0xFF);
                }
            }
            playBuffer(outputBuffer);
        }
    }
