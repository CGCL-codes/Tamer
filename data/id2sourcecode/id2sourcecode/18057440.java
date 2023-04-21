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
