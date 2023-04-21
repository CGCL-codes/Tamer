    private void convertInputData(byte[] buffer) {
        AudioFormat.Encoding afe = targetLine.getFormat().getEncoding();
        AudioFormat format = targetLine.getFormat();
        int sampleSize = format.getSampleSizeInBits();
        int frameSize = format.getFrameSize();
        boolean bigEndian = format.isBigEndian();
        int channelNumber = format.getChannels();
        double[] data = new double[buffer.length / frameSize];
        double temp = 0;
        int j = 0;
        switch(sampleSize) {
            case 8:
                if (afe == AudioFormat.Encoding.PCM_SIGNED) for (int i = 0; i < buffer.length; i += channelNumber) {
                    temp = 0;
                    for (int c = 0; c < channelNumber; c++) temp += buffer[i + c];
                    data[j++] = temp / channelNumber;
                } else if (afe == AudioFormat.Encoding.PCM_UNSIGNED) for (int i = 0; i < buffer.length; i += channelNumber) {
                    temp = 0;
                    for (int c = 0; c < channelNumber; c++) temp += (byte) (buffer[i + c] ^ 0x80) + 128;
                    data[j++] = temp / channelNumber;
                }
                break;
            case 16:
                if (bigEndian) for (int i = 0; i < buffer.length; i += 2 * channelNumber) {
                    temp = 0;
                    for (int c = 0; c < channelNumber; c++) temp += (short) ((buffer[i + c] << 8) | (buffer[i + c + 1]));
                    data[j++] = (temp / channelNumber) / 256;
                } else for (int i = 0; i < buffer.length; i += 2 * channelNumber) {
                    temp = 0;
                    for (int c = 0; c < channelNumber; c++) temp += (short) ((buffer[i + c]) | (buffer[i + c + 1] << 8));
                    data[j++] = (temp / channelNumber) / 256;
                }
        }
        blockingQueue.add(new VariableDataChunk(data));
    }
