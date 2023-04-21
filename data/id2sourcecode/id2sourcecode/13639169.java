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
