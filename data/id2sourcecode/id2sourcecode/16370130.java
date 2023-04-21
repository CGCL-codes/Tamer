    private void init() {
        savedStates = new ArrayList<EEGChannelState>();
        if (settings.isEmpty()) {
            return;
        }
        setDevice(getDeviceFromSettings());
        setSignalProcessor(getSignalProcessorFromSettings());
        setSampleRate(Double.parseDouble(settings.getProperty(SAMPLE_RATE)));
        List<String> frequencyTypeKeys = getFrequencyTypeKeys();
        List<FrequencyType> frequencyTypes = getFrequencyTypeList(frequencyTypeKeys);
        for (FrequencyType type : frequencyTypes) {
            savedStates.add(getChannelState(type));
            fingBrainerStates.put(type, getFingBrainerState(type));
        }
    }
