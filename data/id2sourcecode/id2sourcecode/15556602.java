    private void lst_channelListMouseClicked(java.awt.event.MouseEvent evt) {
        if (!(lst_channelList.getModel().getElementAt(lst_channelList.locationToIndex(evt.getPoint())).toString().equalsIgnoreCase(Globals.getThisPlayer_loginName()))) {
            lst_channelList.setSelectedIndex(lst_channelList.locationToIndex(evt.getPoint()));
        } else {
            lst_channelList.clearSelection();
            return;
        }
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            String name = (String) lst_channelList.getSelectedValue();
            if (name != null && name.length() > 0 && !name.equals(Globals.getThisPlayer_loginName())) {
                if (model.getChannel(name) != null) {
                    VoiceClient.send("cc" + Protocol.MESSAGE_DELIMITER + model.indexOfChannel(model.getChannel(name)));
                } else {
                    if (model.isMuted(name)) {
                        model.unMute(name);
                    } else {
                        model.mute(name);
                    }
                }
            }
        }
    }
