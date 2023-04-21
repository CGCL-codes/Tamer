        public void actionPerformed(java.awt.event.ActionEvent event) {
            if (channelPanel.getParent() == null) {
                settingsPanel.removeAll();
                settingsPanel.add(channelPanel);
                settingsPanel.validate();
            }
            ChannelModel channelModel = scopeModel.getChannelModel(channelIndex);
            channelPanel.setChannelModel((AbstractButton) event.getSource(), channelModel);
            channelPanel.resetDefaultFocus();
        }
