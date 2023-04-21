    boolean addDevice(UPBLinkDevice theLinkedDevice) {
        if (theLinkedDevice.getDevice() instanceof UPBWildcardDevice) return false;
        if (getLinkedDeviceById(theLinkedDevice.getDevice().getDeviceID(), theLinkedDevice.getChannel()) != null) return false;
        int linkCount = theLinkedDevice.getDevice().getLinks().size();
        if (linkCount >= theLinkedDevice.getDevice().getReceiveComponentCount()) return false;
        int insertAt = -1;
        for (int deviceIndex = 0; deviceIndex < linkedDevices.size(); deviceIndex++) {
            if (linkedDevices.get(deviceIndex).getDevice().getDeviceID() > theLinkedDevice.getDevice().getDeviceID()) {
                insertAt = deviceIndex;
                linkedDevices.add(deviceIndex, theLinkedDevice);
                break;
            }
        }
        if (insertAt == -1) linkedDevices.add(theLinkedDevice);
        insertAt = -1;
        for (int linkIndex = 0; linkIndex < theLinkedDevice.getDevice().getLinks().size(); linkIndex++) {
            if (theLinkedDevice.getDevice().getLinks().get(linkIndex).getLink().getLinkID() > linkID) {
                insertAt = linkIndex;
                theLinkedDevice.getDevice().getLinks().add(linkIndex, theLinkedDevice);
                break;
            }
        }
        if (insertAt == -1) theLinkedDevice.getDevice().getLinks().add(theLinkedDevice);
        linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_DEVICE_ADDED, theLinkedDevice);
        linkNetwork.getUPBManager().fireDeviceEvent(theLinkedDevice.getDevice(), org.cdp1802.upb.UPBDeviceEvent.EventCode.LINK_ADDED, theLinkedDevice);
        return true;
    }
