    private void getChannelSequence() {
        if (Utility.isEmpty(disableViewChannel)) {
            disableViewChannel = UserHomePreferenceConstant.SYSTEMPREFERENCESTR;
        }
        List channelSequenceList = preferenceMg.getSpecialChannelSequence(disableViewChannel);
        if (channelSequenceList == null) channelSequenceList = new ArrayList();
        this.setChannelSquence(channelSequenceList);
    }
