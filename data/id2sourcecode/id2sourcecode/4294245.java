    public boolean removeDevice(UPBDeviceI theDevice, int theChannel) {
        UPBLinkDevice linkedDevice = null;
        boolean deviceRemoved = false;
        for (int deviceIndex = linkedDevices.size() - 1; deviceIndex >= 0; deviceIndex--) {
            if ((linkedDevice = linkedDevices.get(deviceIndex)).getDevice() != theDevice) continue;
            if ((theChannel != -1) && (theChannel != linkedDevice.getChannel())) continue;
            linkedDevices.remove(deviceIndex);
            theDevice.removeLinkDevice(linkedDevice);
            linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_DEVICE_REMOVED, linkedDevice);
            linkNetwork.getUPBManager().fireDeviceEvent(theDevice, org.cdp1802.upb.UPBDeviceEvent.EventCode.LINK_REMOVED, linkedDevice);
            linkedDevice.releaseResources();
            deviceRemoved = true;
        }
        return deviceRemoved;
    }
