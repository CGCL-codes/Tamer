    UPBDeviceEvent(UPBDeviceI theDevice, EventCode theEventCode, UPBLinkDevice theLinkDevice) {
        this.eventDevice = theDevice;
        this.eventLinkDevice = theLinkDevice;
        this.eventLink = theLinkDevice.getLink();
        this.eventCode = theEventCode;
        this.eventChannel = theLinkDevice.getChannel();
    }
