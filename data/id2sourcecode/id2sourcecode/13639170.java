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
