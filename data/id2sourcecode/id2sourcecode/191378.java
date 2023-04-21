    public int read(byte[] abData, int nOffset, int nLength) throws IOException {
        mixBuffer.changeSampleCount(nLength / getFormat().getFrameSize(), false);
        mixBuffer.makeSilence();
        int maxMixed = 0;
        Iterator streamIterator = audioInputStreamList.iterator();
        while (streamIterator.hasNext()) {
            AudioInputStream stream = (AudioInputStream) streamIterator.next();
            int needRead = mixBuffer.getSampleCount() * stream.getFormat().getFrameSize();
            if (tempBuffer == null || tempBuffer.length < needRead) {
                tempBuffer = new byte[needRead];
            }
            int bytesRead = stream.read(tempBuffer, 0, needRead);
            if (bytesRead == -1) {
                streamIterator.remove();
                continue;
            }
            readBuffer.initFromByteArray(tempBuffer, 0, bytesRead, stream.getFormat());
            if (maxMixed < readBuffer.getSampleCount()) {
                maxMixed = readBuffer.getSampleCount();
            }
            int maxChannels = Math.min(mixBuffer.getChannelCount(), readBuffer.getChannelCount());
            for (int channel = 0; channel < maxChannels; channel++) {
                float[] readSamples = readBuffer.getChannel(channel);
                float[] mixSamples = mixBuffer.getChannel(channel);
                int maxSamples = Math.min(mixBuffer.getSampleCount(), readBuffer.getSampleCount());
                for (int sample = 0; sample < maxSamples; sample++) {
                    mixSamples[sample] += attenuationFactor * readSamples[sample];
                }
            }
        }
        if (maxMixed == 0) {
            if (audioInputStreamList.size() == 0) {
                return -1;
            }
            return 0;
        }
        mixBuffer.convertToByteArray(0, maxMixed, abData, nOffset, getFormat());
        return maxMixed * getFormat().getFrameSize();
    }
