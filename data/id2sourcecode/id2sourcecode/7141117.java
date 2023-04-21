    public void setDeviceInfo(UPBNetworkI theNetwork, UPBProductI theProduct, int deviceID) {
        super.setDeviceInfo(theNetwork, theProduct, deviceID);
        defaultFadeRate = new int[(getChannelCount() < 1) ? 1 : getChannelCount()];
        for (int chanIndex = 0; chanIndex < deviceState.length; chanIndex++) {
            defaultFadeRate[chanIndex] = 0;
        }
    }
