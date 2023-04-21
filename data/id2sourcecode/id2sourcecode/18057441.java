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
