    public Data[] getData(int bufferSize) throws IOException {
        if (bufferSize > info.getMaxBufferSize()) throw new IllegalArgumentException("Buffer size too large");
        NXTData[] data = new NXTData[channelInfos.length];
        for (int i = 0; i < channelInfos.length; i++) {
            data[i] = new NXTData(channelInfos[i], bufferSize);
        }
        for (int i = 0; i < bufferSize; i++) {
            for (int j = 0; j < channelInfos.length; j++) {
                data[j].setIntData(i, getChannelData(channelInfos[j]));
            }
        }
        return data;
    }
