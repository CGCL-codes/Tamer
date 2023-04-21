                public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
                    ActionManager.getInstance().getParkAction().setEnabled(false);
                    ActionManager.getInstance().getUnparkAction().setEnabled(false);
                    ActionManager.getInstance().getDeleteChannelAction().setEnabled(false);
                    ActionManager.getInstance().getCreateCategoryAction().setEnabled(false);
                    ActionManager.getInstance().getMultiRenameAction().setEnabled(false);
                    jMenu.setEnabled(false);
                    TreePath treePath = e.getNewLeadSelectionPath();
                    if (treePath != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                        if (node != null) {
                            ChannelElement channelElement = (ChannelElement) node.getUserObject();
                            ActionManager.getInstance().getParkAction().setEnabled(channelElement.isRadioOrTelevisionOrService() || channelElement.isCategory());
                            boolean unpargFlag = (channelElement.isRadioOrTelevisionOrService() || channelElement.isCategory()) && (ChannelEditor.application.getChannelParkingPanel().getListSize() > 0);
                            ActionManager.getInstance().getUnparkAction().setEnabled(unpargFlag);
                            ActionManager.getInstance().getDeleteChannelAction().setEnabled(channelElement.isRadioOrTelevisionOrService() || channelElement.isCategory());
                            ActionManager.getInstance().getCreateCategoryAction().setEnabled(node.isRoot());
                            ActionManager.getInstance().getMultiRenameAction().setEnabled(node.isRoot() || channelElement.isCategory());
                            jMenu.setEnabled(channelElement.isCategory() || node.isRoot());
                            ChannelEditor.application.getChannelPropertyPanel().updateFields(channelElement);
                        }
                    } else {
                        ChannelEditor.application.getChannelPropertyPanel().updateFields(ChannelEditor.nothingSelectedChannel);
                    }
                }
