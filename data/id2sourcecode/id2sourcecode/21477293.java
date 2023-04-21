    public IRCChannel[] getChannels() {
        if (customChannelRadioButton.isSelected()) {
        }
        ArrayList<IRCChannel> list = new ArrayList<IRCChannel>(favouritesTable.getSelectedRowCount());
        return list.toArray(new IRCChannel[0]);
    }
