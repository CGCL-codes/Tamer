    public void receiveMessage(UPBMessage theMessage) {
        int theLevel = 0, theFadeRate = 0, theChannel = 0;
        switch(theMessage.getMsgType()) {
            case ACTIVATE_LINK:
                if (UPBManager.DEBUG_MODE) debug("Got Link ACTIVATE");
                linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_ACTIVATED, DEFAULT_DIM_LEVEL, DEFAULT_FADE_RATE, theMessage.getDevice());
                for (UPBLinkDevice linkedDevice : linkedDevices) {
                    linkedDevice.getDevice().updateInternalDeviceLevel(linkedDevice.getLevel(), linkedDevice.getFadeRate(), linkedDevice.getChannel());
                }
                break;
            case DEACTIVATE_LINK:
                if (UPBManager.DEBUG_MODE) debug("Got Link DEACTIVATE");
                linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_DEACTIVATED, DEFAULT_DIM_LEVEL, DEFAULT_FADE_RATE, theMessage.getDevice());
                for (UPBLinkDevice linkedDevice : linkedDevices) {
                    linkedDevice.getDevice().updateInternalDeviceLevel(0, linkedDevice.getFadeRate(), linkedDevice.getChannel());
                }
                break;
            case GOTO:
                if (UPBManager.DEBUG_MODE) debug("Got Link GOTO");
                theLevel = theMessage.getLevel();
                theFadeRate = theMessage.getFadeRate();
                theChannel = theMessage.getChannel();
                linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_GOTO, theLevel, theMessage.getFadeRate(), theMessage.getDevice());
                for (UPBLinkDevice linkedDevice : linkedDevices) {
                    linkedDevice.getDevice().updateInternalDeviceLevel(theLevel, theFadeRate, theChannel);
                }
                break;
            case FADE_START:
                if (UPBManager.DEBUG_MODE) debug("Got Link START FADE");
                theLevel = theMessage.getLevel();
                theFadeRate = theMessage.getFadeRate();
                theChannel = theMessage.getChannel();
                linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_START_FADE, theLevel, theMessage.getFadeRate(), theMessage.getDevice());
                for (UPBLinkDevice linkedDevice : linkedDevices) {
                    linkedDevice.getDevice().updateInternalDeviceLevel(theLevel, theFadeRate, theChannel);
                }
                break;
            case FADE_STOP:
                if (UPBManager.DEBUG_MODE) debug("Got Link STOP FADE");
                linkNetwork.getUPBManager().fireLinkEvent(this, UPBLinkEvent.EventCode.LINK_STOP_FADE, theMessage.getDevice());
                for (UPBLinkDevice linkedDevice : linkedDevices) {
                    linkNetwork.getUPBManager().queueStateRequest(linkedDevice.getDevice());
                }
                break;
            default:
                if (UPBManager.DEBUG_MODE) debug("Got an unhandled report code of " + theMessage.getMsgType().toString());
                break;
        }
    }
