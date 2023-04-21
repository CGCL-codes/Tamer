    private NXTData[] createData() {
        NXTChannelInfo[] channelInfos = (NXTChannelInfo[]) sensor.getSensorInfo().getChannelInfos();
        NXTData[] data = new NXTData[channelInfos.length];
        for (int i = 0; i < channelInfos.length; i++) {
            data[i] = new NXTData(channelInfos[i], bufferSize);
        }
        return data;
    }
