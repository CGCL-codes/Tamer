    public void activateLink() {
        if (!linkNetwork.getUPBManager().sendConfirmedMessage(new UPBMessage(this, UPBMsgType.ACTIVATE_LINK))) return;
        linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_ACTIVATED);
        for (UPBLinkDevice linkedDevice : linkedDevices) {
            linkedDevice.getDevice().updateInternalDeviceLevel(linkedDevice.getLevel(), linkedDevice.getFadeRate(), linkedDevice.getChannel());
        }
    }
