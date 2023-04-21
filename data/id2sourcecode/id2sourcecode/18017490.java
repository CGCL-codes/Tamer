    public int[] getChannelsNotInGroup() {
        Groups groups = getContext().getShow().getGroups();
        boolean any = notInAnyGroup.isSelected();
        int groupIndex = getSelectedGroupIndex();
        int totalChannelCount = getContext().getShow().getNumberOfChannels();
        int[] indexes = groups.getChannelsNotInGroup(any, groupIndex, totalChannelCount);
        return indexes;
    }
