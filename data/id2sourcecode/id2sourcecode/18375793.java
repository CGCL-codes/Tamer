    public String getChannelName(int channelIndex) {
        try {
            return getChannelBindings().get(channelIndex).boundChannelName;
        } catch (Exception e) {
            return null;
        }
    }
