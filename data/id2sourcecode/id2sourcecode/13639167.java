    private void makePVsSelectionPanel() {
        root_Node = new PVTreeNode(root_Name);
        rootPhasePV_Node = new PVTreeNode(rootPhasePV_Name);
        rootXposPV_Node = new PVTreeNode(rootXposPV_Name);
        rootYposPV_Node = new PVTreeNode(rootYposPV_Name);
        rootPhasePV_Node.setPVNamesAllowed(true);
        rootXposPV_Node.setPVNamesAllowed(true);
        rootYposPV_Node.setPVNamesAllowed(true);
        root_Node.add(rootXposPV_Node);
        root_Node.add(rootYposPV_Node);
        root_Node.add(rootPhasePV_Node);
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
                BpmViewerPV bpmPV = null;
                if (pvn_parent == rootPhasePV_Node) {
                    index = pvn_parent.getIndex(pvn);
                    bpmPV = (BpmViewerPV) phasePVs.get(index);
                }
                if (pvn_parent == rootXposPV_Node) {
                    index = pvn_parent.getIndex(pvn);
                    bpmPV = (BpmViewerPV) xPosPVs.get(index);
                }
                if (pvn_parent == rootYposPV_Node) {
                    index = pvn_parent.getIndex(pvn);
                    bpmPV = (BpmViewerPV) yPosPVs.get(index);
                }
                if (index >= 0 && bpmPV != null) {
                    bpmPV.getArrayDataPV().setSwitchOn(switchOnLocal);
                    updateGraphPanels();
                }
            }
        };
        createDeletePVTreeListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PVTreeNode pvn = (PVTreeNode) e.getSource();
                PVTreeNode pvn_parent = (PVTreeNode) pvn.getParent();
                String command = e.getActionCommand();
                boolean bool_removePV = command.equals(PVTreeNode.REMOVE_PV_COMMAND);
                int index = -1;
                BpmViewerPV pv_tmp = null;
                if (bool_removePV) {
                    if (pvn_parent == rootPhasePV_Node) {
                        index = pvn_parent.getIndex(pvn);
                        pv_tmp = (BpmViewerPV) phasePVs.get(index);
                        phasePVs.remove(pv_tmp);
                    }
                    if (pvn_parent == rootXposPV_Node) {
                        index = pvn_parent.getIndex(pvn);
                        pv_tmp = (BpmViewerPV) xPosPVs.get(index);
                        xPosPVs.remove(pv_tmp);
                    }
                    if (pvn_parent == rootYposPV_Node) {
                        index = pvn_parent.getIndex(pvn);
                        pv_tmp = (BpmViewerPV) yPosPVs.get(index);
                        yPosPVs.remove(pv_tmp);
                    }
                    if (index >= 0) {
                        updatingController.removeArrayDataPV(pv_tmp.getArrayDataPV());
                        setColors(pvn_parent, index);
                        updateGraphPanels();
                    }
                } else {
                    if (pvn_parent == rootPhasePV_Node) {
                        index = pvn_parent.getIndex(pvn);
                        pv_tmp = new BpmViewerPV(phaseGraphs);
                        phasePVs.add(index, pv_tmp);
                    }
                    if (pvn_parent == rootXposPV_Node) {
                        index = pvn_parent.getIndex(pvn);
                        pv_tmp = new BpmViewerPV(xPosGraphs);
                        xPosPVs.add(index, pv_tmp);
                    }
                    if (pvn_parent == rootYposPV_Node) {
                        index = pvn_parent.getIndex(pvn);
                        pv_tmp = new BpmViewerPV(yPosGraphs);
                        yPosPVs.add(index, pv_tmp);
                    }
                    if (index >= 0) {
                        pv_tmp.setChannel(pvn.getChannel());
                        updatingController.addArrayDataPV(pv_tmp.getArrayDataPV());
                        setColors(pvn_parent, -1);
                        updateGraphPanels();
                    }
                }
            }
        };
        renamePVTreeListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PVTreeNode pvn = (PVTreeNode) e.getSource();
                PVTreeNode pvn_parent = (PVTreeNode) pvn.getParent();
                int index = -1;
                BpmViewerPV pv_tmp = null;
                if (pvn_parent == rootPhasePV_Node) {
                    index = pvn_parent.getIndex(pvn);
                    pv_tmp = (BpmViewerPV) phasePVs.get(index);
                }
                if (pvn_parent == rootXposPV_Node) {
                    index = pvn_parent.getIndex(pvn);
                    pv_tmp = (BpmViewerPV) xPosPVs.get(index);
                }
                if (pvn_parent == rootYposPV_Node) {
                    index = pvn_parent.getIndex(pvn);
                    pv_tmp = (BpmViewerPV) yPosPVs.get(index);
                }
                if (index >= 0) {
                    pv_tmp.setChannel(pvn.getChannel());
                    setColors(pvn_parent, -1);
                    updateGraphPanels();
                }
            }
        };
        rootPhasePV_Node.setSwitchedOnOffListener(switchPVTreeListener);
        rootXposPV_Node.setSwitchedOnOffListener(switchPVTreeListener);
        rootYposPV_Node.setSwitchedOnOffListener(switchPVTreeListener);
        rootPhasePV_Node.setCreateRemoveListener(createDeletePVTreeListener);
        rootXposPV_Node.setCreateRemoveListener(createDeletePVTreeListener);
        rootYposPV_Node.setCreateRemoveListener(createDeletePVTreeListener);
        rootPhasePV_Node.setRenameListener(renamePVTreeListener);
        rootXposPV_Node.setRenameListener(renamePVTreeListener);
        rootYposPV_Node.setRenameListener(renamePVTreeListener);
    }
