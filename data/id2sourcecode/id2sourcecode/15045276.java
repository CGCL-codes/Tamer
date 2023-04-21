    private void fillData() {
        Iterator it;
        it = editChannelsSet.getChannels().iterator();
        while (it.hasNext()) {
            csetModel.addElement(it.next());
        }
        it = allChannels.getSortedChannels().iterator();
        while (it.hasNext()) {
            TVChannelsSet.Channel ch = (TVChannelsSet.Channel) it.next();
            if (!editChannelsSet.contains(ch.getChannelID())) {
                allModel.addElement(ch);
            }
        }
        nameTextField.setText(editChannelsSet.getName());
    }
