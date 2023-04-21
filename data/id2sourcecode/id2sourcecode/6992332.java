    private EEGChannelValue getChannelValue(FrequencyType frequencyType) {
        for (EEGChannelValue value : currentChannelValues) {
            if (value.isForFrequencyType(frequencyType)) {
                return value;
            }
        }
        return null;
    }
