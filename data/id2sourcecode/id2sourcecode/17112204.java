    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        sweepComboBoxActionPerformed(evt);
        int[] indices = sweepList.getSelectedIndices();
        DataFrame ifr = (DataFrame) desktop.getActiveFrame();
        if (ifr != null) {
            DataStorage source = ifr.getDataContent().getView().getStorage();
            for (int i = 0; i < source.getGroupsSize(); i++) {
                boolean visible = false;
                for (int k = 0; k < indices.length; k++) {
                    if (indices[k] == i) visible = true;
                }
                for (int j = 0; j < source.getChannelsSize(i); j++) {
                    DataChannel chan = source.getChannel(i, j);
                    if (visible) {
                        chan.getAttribute().setVisiblity(ChannelAttribute.NORMAL_CHANNEL);
                    } else {
                        chan.getAttribute().setVisiblity(ChannelAttribute.HIDDEN_CHANNEL);
                    }
                }
            }
        }
        doClose(RET_OK);
        ifr.getDataContent().getLegend().getTreeComponent().repaint();
        ifr.getDataContent().getView().repaint();
    }
