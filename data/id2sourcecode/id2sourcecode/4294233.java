    public void startLinkFade(int toLevel, int atFadeRate) {
        if (!linkNetwork.getUPBManager().sendConfirmedMessage(new UPBMessage(this, UPBMsgType.FADE_START, toLevel, atFadeRate))) return;
        linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_START_FADE, toLevel, atFadeRate, null);
        for (UPBLinkDevice linkedDevice : linkedDevices) {
            linkedDevice.getDevice().updateInternalDeviceLevel(toLevel, atFadeRate, linkedDevice.getChannel());
        }
    }
