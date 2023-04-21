    private String getChannelVariables() {
        if (numChannels <= 0) {
            return null;
        }
        String info = "aChannel1";
        int i = 1;
        while (i < numChannels) {
            i++;
            info += ", aChannel" + i;
        }
        return info;
    }
