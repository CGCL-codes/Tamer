    public UPBLinkDevice getLinkedDeviceById(int deviceID, int theChannel) {
        for (UPBLinkDevice theLinkedDevice : linkedDevices) {
            if (theLinkedDevice.getDevice().getDeviceID() != deviceID) continue;
            if ((theChannel != -1) && (theChannel != theLinkedDevice.getChannel())) continue;
            return theLinkedDevice;
        }
        return null;
    }
