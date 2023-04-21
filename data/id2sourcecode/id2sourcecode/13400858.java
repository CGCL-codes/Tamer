    protected String getDeviceSignature(DeviceContainer dc) {
        String signature = "";
        int channelCount = getChannelCount(dc.getAddress());
        for (int idx = 0; idx < channelCount; idx++) {
            signature += dc.getSignature() + ":" + idx;
            if (idx < (channelCount - 1)) {
                signature += " ";
            }
        }
        return signature;
    }
