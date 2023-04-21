    public void setDeviceInfo(UPBNetworkI theNetwork, UPBProductI theProduct, int deviceID) {
        super.setDeviceInfo(theNetwork, theProduct, deviceID);
        ioState = new boolean[getChannelCount()];
    }
