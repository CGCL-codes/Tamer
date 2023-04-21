    public static void cue(final LightCueDetail cue, final String levels) {
        String[] strings = levels.split(" ");
        for (String string : strings) {
            String[] subStrings = string.split("@");
            if (subStrings[0].startsWith("s")) {
                int submasterIndex = Util.toInt(subStrings[0].substring(1));
                CueSubmasterLevel level = cue.getSubmasterLevel(submasterIndex);
                LevelValue levelValue = level.getLevelValue();
                updateLevel(level, levelValue, subStrings[1]);
            } else {
                int channelIndex = Util.toInt(subStrings[0]);
                CueChannelLevel level = cue.getChannelLevel(channelIndex);
                LevelValue levelValue = level.getChannelLevelValue();
                updateLevel(level, levelValue, subStrings[1]);
            }
        }
    }
