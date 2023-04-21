    protected void updateChannels() {
        availableChannels.setSelectedIndex(-1);
        List<ExperimentChannel> availableChannelList = new ArrayList<ExperimentChannel>(devicePool.getExperimentChannels());
        List<ExperimentChannel> reservedChannelList = measurementGroup.getChannels();
        availableChannelList.removeAll(reservedChannelList);
        ((DefaultListModel) availableChannels.getModel()).removeAllElements();
        for (ExperimentChannel channel : availableChannelList) ((DefaultListModel) availableChannels.getModel()).addElement(channel);
    }
