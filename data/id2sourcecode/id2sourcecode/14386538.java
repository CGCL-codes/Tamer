    public void actionPerformed(ActionEvent e) {
        TreePath targetPath = ChannelEditor.application.getChannelListingPanel().getLeadSelectionPath();
        if (targetPath != null) {
            DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) targetPath.getLastPathComponent();
            ChannelElement channelElementTarget = (ChannelElement) targetNode.getUserObject();
            Object[] sources = ChannelEditor.application.getChannelParkingPanel().getSelectedOrAllElements();
            if (sources != null) {
                for (int i = 0; i < sources.length; i++) {
                    DefaultMutableTreeNode sourceNode = (DefaultMutableTreeNode) sources[i];
                    ChannelElement channelElementSource = (ChannelElement) sourceNode.getUserObject();
                    if (channelElementSource.isRadioOrTelevisionOrService() && channelElementTarget.isCategory()) {
                        ChannelEditor.application.getChannelListingPanel().insertNodeInto(sourceNode, targetNode, targetNode.getChildCount());
                    } else {
                        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) targetNode.getParent();
                        int pos = parentNode.getIndex(targetNode);
                        ChannelEditor.application.getChannelListingPanel().insertNodeInto(sourceNode, parentNode, pos);
                    }
                    ChannelEditor.application.getChannelParkingPanel().removeElement(sourceNode);
                }
                ChannelEditor.application.setModified(true);
            }
        }
    }
