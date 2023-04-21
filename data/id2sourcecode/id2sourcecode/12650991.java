    public void deactivateChannel(final int lightCueIndex, final int channelIndex) {
        LightCueDetail detail = getDetail(lightCueIndex);
        CueChannelLevel level = detail.getChannelLevel(channelIndex);
        LevelValue levelValue = level.getChannelLevelValue();
        level.setDerived(false);
        levelValue.setActive(false);
        boolean foundNonDerived = false;
        for (int i = lightCueIndex + 1; !foundNonDerived && i < size(); i++) {
            CueChannelLevel next = getDetail(i).getChannelLevel(channelIndex);
            if (next.isDerived()) {
                LevelValue levelValue2 = next.getChannelLevelValue();
                levelValue2.setActive(false);
                levelValue2.setValue(0f);
                next.setSubmasterValue(0f);
                fireChannelLevelChanged(i, channelIndex);
            } else {
                foundNonDerived = true;
            }
        }
        markDirty();
    }
