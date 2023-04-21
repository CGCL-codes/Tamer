    public void deactivateLink() {
        if (!linkNetwork.getUPBManager().sendConfirmedMessage(new UPBMessage(this, UPBMsgType.DEACTIVATE_LINK))) return;
        linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_DEACTIVATED);
        for (UPBLinkDevice linkedDevice : linkedDevices) {
            linkedDevice.getDevice().updateInternalDeviceLevel(0, 0, linkedDevice.getChannel());
        }
    }
