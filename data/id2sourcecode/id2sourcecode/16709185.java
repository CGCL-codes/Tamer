            public void actionPerformed(ActionEvent arg0) {
                ActionFactory.getInstance().getDataExportAction().exportData(RBNBUtilities.getAllChannels(treeModel.getChannelTree(), treeModel.isHiddenChannelsVisible()));
            }
