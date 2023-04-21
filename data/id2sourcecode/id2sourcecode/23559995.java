    public void process() {
        if ((System.currentTimeMillis() - lastSampleMillis) >= samplingInterval) {
            NXTChannelInfo[] channelInfos = (NXTChannelInfo[]) sensor.getSensorInfo().getChannelInfos();
            for (int i = 0; i < channelInfos.length; i++) {
                data[i].setIntData(position, sensor.getChannelData(channelInfos[i]));
            }
            if (++position == bufferSize) {
                listener.dataReceived(sensor, data, false);
                data = createData();
                position = 0;
            }
            lastSampleMillis = System.currentTimeMillis();
        }
    }
