    @Override
    public synchronized boolean postData(byte[] aSendData, int aChannel) {
        if (isRunning() == false) return false;
        if (aSendData == null) return true;
        if (aSendData.length == 0) return false;
        if (fAttributes.getChannel(aChannel) == null) {
            BthLog.e(TAG, "channel out of range");
            return false;
        }
        return realSend(fBthDataOutputStream, aSendData, aChannel, CommPacket.PACKET_DATA);
    }
