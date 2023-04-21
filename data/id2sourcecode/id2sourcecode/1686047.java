    private int channelIndex(final Dimmer dimmer) {
        int channelIndex = -1;
        if (dimmer.getChannel() != null) {
            channelIndex = dimmer.getChannel().getId();
        } else if (isPrePatch()) {
            channelIndex = dimmer.getId() + PRE_PATCH_START;
        }
        return channelIndex;
    }
