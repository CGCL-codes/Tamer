    protected byte[] getExpectedHeaderData(AudioFormat audioFormat, int nLength, boolean bSeekable, boolean bLengthGiven) {
        int nSampleRate = (int) audioFormat.getSampleRate();
        byte[] abExpectedHeaderData = new byte[] { 0x2e, 0x73, 0x6e, 0x64, 0, 0, 0, (byte) (24 + getExpectedAdditionalHeaderLength()), 0, 0, 0, 0, 0, 0, 0, getEncoding(audioFormat), 0, (byte) (nSampleRate / 65536), (byte) (nSampleRate / 256), (byte) nSampleRate, 0, 0, 0, (byte) audioFormat.getChannels() };
        if (bLengthGiven || bSeekable) {
            abExpectedHeaderData[11] = (byte) nLength;
        } else {
            abExpectedHeaderData[8] = (byte) 0xff;
            abExpectedHeaderData[9] = (byte) 0xff;
            abExpectedHeaderData[10] = (byte) 0xff;
            abExpectedHeaderData[11] = (byte) 0xff;
        }
        return abExpectedHeaderData;
    }
