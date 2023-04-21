            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showMessage(null);
                PVTreeNode pvNode = PVTreeNode.getSelectedPVTreeNode(pvRoot);
                if (pvNode == null || !pvNode.isPVName()) {
                    showMessage("Please, select PV in the right tree! You can delete PV only!");
                    return;
                }
                PVTreeNode pvNode_Parent = (PVTreeNode) pvNode.getParent();
                pvNode.removingOccurred();
                pvNode_Parent.remove(pvNode);
                Channel channel = pvNode.getChannel();
                String pv_name = null;
                if (channel != null) {
                    pv_name = channel.channelName();
                } else {
                    pv_name = pvNode.getName();
                }
                pvNameJText.setText(pv_name);
                JTree pvTree = pvTreePanel.getJTree();
                DefaultTreeModel treeModel = (DefaultTreeModel) pvTree.getModel();
                treeModel.reload(pvNode_Parent);
            }
