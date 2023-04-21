    private void makePVsSelectionPanel() {
        root_Node = new PVTreeNode(root_Name);
        rootArrayPV_Node = new PVTreeNode(rootArrayPV_Name);
        rootArrayPV_Node.setPVNamesAllowed(true);
        root_Node.add(rootArrayPV_Node);
        pvsSelector = new PVsSelector(root_Node);
        pvsSelector.removeMessageTextField();
        setPVsPanel.setLayout(new BorderLayout());
        setPVsPanel.add(pvsSelector, BorderLayout.CENTER);
        switchPVTreeListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                PVTreeNode pvn = (PVTreeNode) e.getSource();
                boolean switchOnLocal = command.equals(PVTreeNode.SWITCHED_ON_COMMAND);
                PVTreeNode pvn_parent = (PVTreeNode) pvn.getParent();
                int index = -1;
                ArrayViewerPV arrPV = null;
                if (pvn_parent == rootArrayPV_Node) {
                    index = pvn_parent.getIndex(pvn);
                    arrPV = (ArrayViewerPV) arrayPVs.get(index);
                }
                if (index >= 0 && arrPV != null) {
                    arrPV.getArrayDataPV().setSwitchOn(switchOnLocal);
                    updateGraphPanel();
                }
                viewPanel.validate();
                viewPanel.repaint();
            }
        };
        createDeletePVTreeListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PVTreeNode pvn = (PVTreeNode) e.getSource();
                PVTreeNode pvn_parent = (PVTreeNode) pvn.getParent();
                String command = e.getActionCommand();
                boolean bool_removePV = command.equals(PVTreeNode.REMOVE_PV_COMMAND);
                int index = -1;
                ArrayViewerPV pv_tmp = null;
                if (bool_removePV) {
                    if (pvn_parent == rootArrayPV_Node) {
                        index = pvn_parent.getIndex(pvn);
                        pv_tmp = (ArrayViewerPV) arrayPVs.get(index);
                        arrayPVs.remove(pv_tmp);
                    }
                    if (index >= 0) {
                        updatingController.removeArrayDataPV(pv_tmp.getArrayDataPV());
                        setColors(pvn_parent, index);
                        updateGraphPanel();
                    }
                } else {
                    if (pvn_parent == rootArrayPV_Node) {
                        index = pvn_parent.getIndex(pvn);
                        pv_tmp = new ArrayViewerPV(arrayPVGraphs);
                        arrayPVs.add(index, pv_tmp);
                    }
                    if (index >= 0) {
                        pv_tmp.setChannel(pvn.getChannel());
                        updatingController.addArrayDataPV(pv_tmp.getArrayDataPV());
                        setColors(pvn_parent, -1);
                        updateGraphPanel();
                    }
                }
            }
        };
        renamePVTreeListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PVTreeNode pvn = (PVTreeNode) e.getSource();
                PVTreeNode pvn_parent = (PVTreeNode) pvn.getParent();
                int index = -1;
                ArrayViewerPV pv_tmp = null;
                if (pvn_parent == rootArrayPV_Node) {
                    index = pvn_parent.getIndex(pvn);
                    pv_tmp = (ArrayViewerPV) arrayPVs.get(index);
                }
                if (index >= 0) {
                    pv_tmp.setChannel(pvn.getChannel());
                    setColors(pvn_parent, -1);
                    updateGraphPanel();
                }
            }
        };
        rootArrayPV_Node.setSwitchedOnOffListener(switchPVTreeListener);
        rootArrayPV_Node.setCreateRemoveListener(createDeletePVTreeListener);
        rootArrayPV_Node.setRenameListener(renamePVTreeListener);
    }
