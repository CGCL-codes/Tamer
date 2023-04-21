    private void parseCueChannelLevel() {
        if (lightCueDetail == null) {
            error("lightCueDetail not found");
        } else {
            int channelId = getInt("fixture-id");
            if (channelId <= 0 || channelId > show.getChannels().size()) {
                error("Unknown fixture with id \"" + channelId + "\"");
            } else {
                String value = getString("value");
                CueChannelLevel level = lightCueDetail.getChannelLevel(channelId - 1);
                LevelValue levelValue = level.getChannelLevelValue();
                levelValue.setActive(true);
                if ("derived".equals(value)) {
                    level.setDerived(true);
                    levelValue.setIntValue(0);
                } else {
                    level.setDerived(false);
                    levelValue.setIntValue(Util.toInt(value));
                }
            }
        }
    }
