    private void setChannelDerived(final int cueIndex, final int channelIndex, final boolean derived) {
        lightCues.getDetail(cueIndex).getChannelLevel(channelIndex).setDerived(derived);
    }
