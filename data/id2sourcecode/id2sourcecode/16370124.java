    public void saveSettings(EEGDevice device, Map<FrequencyType, FingBrainerState> fingBrainerStates) {
        setSampleRate(device.getSampleFrequencyInHertz());
        setDevice(device);
        setSignalProcessor(device.getSignalProcessor());
        this.savedStates = device.getChannelStates();
        this.fingBrainerStates = fingBrainerStates;
        saveSettings();
    }
