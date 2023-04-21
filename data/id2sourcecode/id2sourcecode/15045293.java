    private void updateChannelSet() {
        editChannelsSet.setName(this.nameTextField.getText());
        editChannelsSet.getChannels().clear();
        for (int i = 0; i < csetModel.size(); i++) {
            editChannelsSet.add((TVChannelsSet.Channel) csetModel.get(i));
        }
        setSave();
    }
