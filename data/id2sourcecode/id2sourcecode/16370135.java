    private EEGChannelState getChannelState(FrequencyType type) {
        String keyPart = type.getDescription().replaceAll(" ", "_") + ".";
        double rangeFrom = Double.parseDouble(settings.getProperty(keyPart + RANGE_FROM));
        double rangeTo = Double.parseDouble(settings.getProperty(keyPart + RANGE_TO));
        boolean highPass = Boolean.parseBoolean(settings.getProperty(keyPart + HIGH_PASS));
        boolean lowPass = Boolean.parseBoolean(settings.getProperty(keyPart + LOW_PASS));
        EEGChannelState state = new EEGChannelState(type, rangeFrom, rangeTo, getSampleRate());
        state.setHighPass(highPass);
        state.setLowPass(lowPass);
        return state;
    }
