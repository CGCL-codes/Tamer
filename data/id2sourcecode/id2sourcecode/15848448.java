    public PVsSelector(PVTreeNode pvRootIn) {
        pvRoot = pvRootIn;
        pvTreePanel = new PVsTreePanel(pvRoot);
        pvTreePanel.setEditMode();
        fnt = getFont();
        messageJText.setEditable(false);
        messageJText.setForeground(Color.red);
        pvNameJText.setEditable(false);
        setPVButton.setHorizontalAlignment(JButton.RIGHT);
        removePVButton.setHorizontalAlignment(JButton.RIGHT);
        setLayout(new BorderLayout());
        uppPanel.setLayout(new BorderLayout());
        uppPanel.add(messageJText, BorderLayout.NORTH);
        uppPanel.add(rawPVButton, BorderLayout.WEST);
        uppPanel.add(pvNameJText, BorderLayout.CENTER);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(0, 2, 0, 0));
        JPanel centerLeftPanel = new JPanel();
        centerLeftPanel.setLayout(new BorderLayout());
        centerLeftPanel.add(pvTreePanel, BorderLayout.CENTER);
        JPanel tmp = new JPanel();
        tmp.setLayout(new BorderLayout());
        tmp.add(setPVButton, BorderLayout.NORTH);
        tmp.add(removePVButton, BorderLayout.SOUTH);
        JPanel tmp_1 = new JPanel();
        tmp_1.setLayout(new BorderLayout());
        tmp_1.add(tmp, BorderLayout.NORTH);
        JPanel tmp_2 = new JPanel();
        tmp_2.setLayout(new BorderLayout());
        tmp_2.add(tmp_1, BorderLayout.EAST);
        JPanel tmp_3 = new JPanel();
        tmp_3.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tmp_3.add(tmp_2);
        centerLeftPanel.add(tmp_3, BorderLayout.WEST);
        centerPanel.add(scrollPane);
        centerPanel.add(centerLeftPanel);
        add(uppPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        setPVButton.setToolTipText("set PV name into the left PV tree");
        removePVButton.setToolTipText("delete PV name from the left PV tree");
        addPVActionListener = new java.awt.event.ActionListener() {

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
        };
        setPVButton.addActionListener(addPVActionListener);
        pvNameJText.addActionListener(addPVActionListener);
        removePVButton.addActionListener(new java.awt.event.ActionListener() {

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
        });
        rawPVButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pvNameJText.setEditable(rawPVButton.isSelected());
            }
        });
        ActionListener extTreeSelectionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof PVTreeNode) {
                    PVTreeNode tn = (PVTreeNode) source;
                    if (tn.isPVName()) {
                        Channel channel = tn.getChannel();
                        if (channel != null) {
                            pvNameJText.setText(null);
                            pvNameJText.setText(channel.channelName());
                        } else {
                            pvNameJText.setText(null);
                            pvNameJText.setText(tn.getName());
                        }
                    }
                }
            }
        };
        pvTreePanel.setExtTreeSelectionListener(extTreeSelectionListener);
        setAllFonts(fnt);
    }
