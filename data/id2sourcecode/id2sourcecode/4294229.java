    public void setLinkLevel(int toLevel, int atFadeRate) {
        if (!linkNetwork.getUPBManager().sendConfirmedMessage(new UPBMessage(this, UPBMsgType.GOTO, toLevel, atFadeRate))) return;
        linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_GOTO, toLevel, atFadeRate, null);
        for (UPBLinkDevice linkedDevice : linkedDevices) {
            linkedDevice.getDevice().updateInternalDeviceLevel(toLevel, linkedDevice.getFadeRate(), linkedDevice.getChannel());
        }
    }
