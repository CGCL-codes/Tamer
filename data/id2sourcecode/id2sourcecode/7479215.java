    private JTree getJTree() {
        if (jTree == null) {
            jTree = new DNDTree();
            setDefaultTreeModel(new DefaultMutableTreeNode(new ChannelElement(Messages.getString("ChannelListingPanel.0"))));
            ChannelTreeRenderer channelRenderer = new ChannelTreeRenderer();
            jTree.setCellRenderer(channelRenderer);
            jTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK), "parkAction");
            jTree.getActionMap().put("parkAction", ActionManager.getInstance().getParkAction());
            jTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK), "unparkAction");
            jTree.getInputMap().put(KeyStroke.getKeyStroke("INSERT"), "unparkAction");
            jTree.getActionMap().put("unparkAction", ActionManager.getInstance().getUnparkAction());
            jTree.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "deleteAction");
            jTree.getActionMap().put("deleteAction", ActionManager.getInstance().getDeleteChannelAction());
            jTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

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
            });
            jTree.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseReleased(java.awt.event.MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }

                public void mousePressed(java.awt.event.MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
        }
        return jTree;
    }
