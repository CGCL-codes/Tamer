    public HomenetHardware(HomenetHardware hardware) {
        this(hardware.getHardwareAddr(), hardware.getHardwareSetupDescription(), hardware.getAppHardwareDescription(), hardware.getNumChannels());
        for (int channel = 0; channel < hardware.getNumChannels(); channel++) this.setChannelDescription(channel, hardware.getChannelDescription(channel));
    }
