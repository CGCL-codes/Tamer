    private synchronized boolean realSend(IBthDataOutputStream aStream, byte[] aRawData, int aChannel, byte aPacketType) {
        if (isRunning() == false) return false;
        fHeartBeatPolicy.pause();
        fChannelCurrentSent = fAttributes.getChannel(aChannel);
        fChannelCurrentSent.fPacketType = aPacketType;
        boolean result = aStream.send(aRawData);
        fHeartBeatPolicy.resume();
        if (result == false) {
            BthLog.e(TAG, "SEND DATA ERROR !!!!");
            unInitialize();
        }
        return result;
    }
