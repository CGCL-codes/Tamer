    protected void updateChannels() {
        availableChannels.setSelectedIndex(-1);
        List<ExperimentChannel> availableChannelList = measurementGroup.getChannels();
        ((DefaultListModel) availableChannels.getModel()).removeAllElements();
        for (ExperimentChannel channel : availableChannelList) ((DefaultListModel) availableChannels.getModel()).addElement(channel);
    }
