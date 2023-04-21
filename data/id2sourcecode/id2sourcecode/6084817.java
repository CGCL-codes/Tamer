    public synchronized int process(Buffer inBuf, Buffer outBuf) {
        int inLength = inBuf.getLength();
        byte[] inData = (byte[]) inBuf.getData();
        byte[] outData = (byte[]) outBuf.getData();
        if (outData == null || outData.length < PACKET_SIZE) {
            outData = new byte[PACKET_SIZE];
            outBuf.setData(outData);
        }
        int rate = (int) inFormat.getSampleRate();
        int size = (int) inFormat.getSampleSizeInBits();
        int channels = (int) inFormat.getChannels();
        outData[0] = 0;
        outData[1] = (byte) ((rate >> 16) & 0xff);
        outData[2] = (byte) ((rate >> 8) & 0xff);
        outData[3] = (byte) (rate & 0xff);
        outData[4] = (byte) inFormat.getSampleSizeInBits();
        outData[5] = (byte) inFormat.getChannels();
        outData[6] = (byte) inFormat.getEndian();
        outData[7] = (byte) inFormat.getSigned();
        int frameSize = inFormat.getSampleSizeInBits() * inFormat.getChannels();
        if (rate != (int) outFormat.getFrameRate() || frameSize != outFormat.getFrameSizeInBits()) {
            outFormat = new AudioFormat(CUSTOM_PCM, AudioFormat.NOT_SPECIFIED, AudioFormat.NOT_SPECIFIED, AudioFormat.NOT_SPECIFIED, AudioFormat.NOT_SPECIFIED, AudioFormat.NOT_SPECIFIED, size * channels, rate, null);
        }
        if (inLength + historyLength >= DATA_SIZE) {
            int copyFromHistory = Math.min(historyLength, DATA_SIZE);
            System.arraycopy(history, 0, outData, HDR_SIZE, copyFromHistory);
            int remainingBytes = DATA_SIZE - copyFromHistory;
            System.arraycopy(inData, inBuf.getOffset(), outData, copyFromHistory + HDR_SIZE, remainingBytes);
            historyLength -= copyFromHistory;
            inBuf.setOffset(inBuf.getOffset() + remainingBytes);
            inBuf.setLength(inLength - remainingBytes);
            outBuf.setFormat(outFormat);
            outBuf.setLength(PACKET_SIZE);
            outBuf.setOffset(0);
            return INPUT_BUFFER_NOT_CONSUMED;
        }
        if (inBuf.isEOM()) {
            System.arraycopy(history, 0, outData, HDR_SIZE, historyLength);
            System.arraycopy(inData, inBuf.getOffset(), outData, historyLength + HDR_SIZE, inLength);
            outBuf.setFormat(outFormat);
            outBuf.setLength(inLength + historyLength + HDR_SIZE);
            outBuf.setOffset(0);
            historyLength = 0;
            return BUFFER_PROCESSED_OK;
        }
        System.arraycopy(inData, inBuf.getOffset(), history, historyLength, inLength);
        historyLength += inLength;
        return OUTPUT_BUFFER_NOT_FILLED;
    }
