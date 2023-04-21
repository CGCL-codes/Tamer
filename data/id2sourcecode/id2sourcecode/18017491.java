    public int getChannelsNotInGroupCount() {
        Groups groups = getContext().getShow().getGroups();
        boolean any = notInAnyGroup.isSelected();
        int groupIndex = getSelectedGroupIndex();
        int totalChannelCount = getContext().getShow().getNumberOfChannels();
        int channelCount = groups.getChannelsNotInGroupCount(any, groupIndex, totalChannelCount);
        return channelCount;
    }
