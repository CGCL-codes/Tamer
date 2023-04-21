    public void setCellAudioData(int nTrack, int nPhase, byte[] abData) {
        int sampleCount = getAudioDataSampleCount(nPhase);
        AudioFormat format = getAudioFormat();
        FloatSampleBuffer fsb = getCellAudioData(nTrack, nPhase);
        if (fsb == null) {
            fsb = new FloatSampleBuffer(format.getChannels(), sampleCount, format.getSampleRate());
        }
        int byteSize = getAudioDataByteLength(nPhase);
        if (byteSize > abData.length) {
            byteSize = abData.length;
        }
        fsb.initFromByteArray(abData, 0, byteSize, format, true);
        setCellAudioData(nTrack, nPhase, fsb);
    }
