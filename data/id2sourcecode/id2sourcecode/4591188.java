    private void calculateChannelLevelValues() {
        for (int channelIndex = 0; channelIndex < channelCount; channelIndex++) {
            float value = DEFAULT_LEVEL;
            boolean active = DEFAULT_ACTIVE;
            for (LightCueDetail detail : cues) {
                CueChannelLevel level = detail.getChannelLevel(channelIndex);
                LevelValue levelValue = level.getChannelLevelValue();
                if (level.isDerived()) {
                    levelValue.setValue(value);
                    levelValue.setActive(active);
                } else {
                    value = levelValue.getValue();
                    active = levelValue.isActive();
                }
            }
        }
    }
