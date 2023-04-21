            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showMessage(null);
                PVTreeNode pvNode = PVTreeNode.getSelectedPVTreeNode(pvRoot);
                if (pvNode == null) {
                    showMessage("Please, select a right place for new PV in the right tree!");
                    return;
                }
                if (rawPVButton.isSelected()) {
                    String pv_name = pvNameJText.getText();
                    if (pv_name.length() == 0) {
                        showMessage("PV name field is empty.");
                        return;
                    }
                    if (pvNode.isPVNamesAllowed() && !pvNode.isPVName()) {
                        if (pvNode.getPVNumberLimit() <= pvNode.getChildCount()) {
                            showMessage("This container can include only n=" + pvNode.getPVNumberLimit() + " PVs and you have n=" + pvNode.getLeafCount());
                            return;
                        }
                        PVTreeNode pvNodeNew = new PVTreeNode(pv_name);
                        Channel channel = ChannelFactory.defaultFactory().getChannel(pv_name);
                        pvNodeNew.setChannel(channel);
                        pvNodeNew.setAsPVName(true);
                        pvNodeNew.setCheckBoxVisible(pvNode.isCheckBoxVisible());
                        pvNode.add(pvNodeNew);
                        JTree pvTree = pvTreePanel.getJTree();
                        DefaultTreeModel treeModel = (DefaultTreeModel) pvTree.getModel();
                        treeModel.reload(pvNode);
                        pvNodeNew.setSwitchedOnOffListener(pvNode.getSwitchedOnOffListener());
                        pvNodeNew.setCreateRemoveListener(pvNode.getCreateRemoveListener());
                        pvNodeNew.setRenameListener(pvNode.getRenameListener());
                        pvNodeNew.creatingOccurred();
                        pvTree.scrollPathToVisible(new TreePath(pvNodeNew.getPath()));
                        return;
                    }
                    if (pvNode.isPVName()) {
                        pvNode.setColor(null);
                        pvNode.setCheckBoxVisible(((PVTreeNode) pvNode.getParent()).isCheckBoxVisible());
                        Channel channel = ChannelFactory.defaultFactory().getChannel(pv_name);
                        pvNode.setChannel(channel);
                        pvNode.setName(pv_name);
                        JTree pvTree = pvTreePanel.getJTree();
                        DefaultTreeModel treeModel = (DefaultTreeModel) pvTree.getModel();
                        treeModel.reload(pvNode.getParent());
                        return;
                    }
                } else {
                    if (pvNode.isPVNamesAllowed() && !pvNode.isPVName()) {
                        if (pvNode.getPVNumberLimit() <= pvNode.getChildCount()) {
                            showMessage("This container can include only n=" + pvNode.getPVNumberLimit() + " PVs and you have n=" + pvNode.getLeafCount());
                            return;
                        }
                        if (selectedHandleNode == null) {
                            showMessage("You did not select PV handler in the left accelerator tree.");
                            return;
                        }
                        PVTreeNode pvNodeNew = new PVTreeNode(selectedHandleNode.getSignalName());
                        pvNodeNew.setChannel(selectedHandleNode.getChannel());
                        pvNodeNew.setAsPVName(true);
                        pvNodeNew.setCheckBoxVisible(pvNode.isCheckBoxVisible());
                        pvNode.add(pvNodeNew);
                        JTree pvTree = pvTreePanel.getJTree();
                        DefaultTreeModel treeModel = (DefaultTreeModel) pvTree.getModel();
                        treeModel.reload(pvNode);
                        pvNodeNew.setSwitchedOnOffListener(pvNode.getSwitchedOnOffListener());
                        pvNodeNew.setCreateRemoveListener(pvNode.getCreateRemoveListener());
                        pvNodeNew.setRenameListener(pvNode.getRenameListener());
                        pvNodeNew.creatingOccurred();
                        pvTree.scrollPathToVisible(new TreePath(pvNodeNew.getPath()));
                        return;
                    }
                    if (pvNode.isPVName()) {
                        pvNode.setColor(null);
                        pvNode.setCheckBoxVisible(((PVTreeNode) pvNode.getParent()).isCheckBoxVisible());
                        pvNode.setChannel(selectedHandleNode.getChannel());
                        pvNode.setName(selectedHandleNode.getSignalName());
                        JTree pvTree = pvTreePanel.getJTree();
                        DefaultTreeModel treeModel = (DefaultTreeModel) pvTree.getModel();
                        treeModel.reload(pvNode.getParent());
                        return;
                    }
                }
            }
