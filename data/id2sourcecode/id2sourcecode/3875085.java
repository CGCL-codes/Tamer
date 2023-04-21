    public void doEditChannelsSets() {
        ChannelSetListDialog dialog = new ChannelSetListDialog(mainFrame, getDataStorage().getInfo().channelsList, config.channelsSetsList);
        Utils.centreDialog(mainFrame, dialog);
        boolean updated = dialog.showDialog();
        if (updated) {
            config.channelsSetsList = dialog.getChannelsSets();
            viewer.onChannelsSetsChanged();
        }
    }
