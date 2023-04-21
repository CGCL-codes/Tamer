    protected void updateMagnetTable() {
        magnetNames.clear();
        B_Sets.clear();
        B_RBs.clear();
        B_Trim_Sets.clear();
        java.util.List<AcceleratorNode> nodes = theDoc.getSelectedSequence().getAllNodes();
        for (AcceleratorNode node : nodes) {
            boolean useIt = false;
            for (String type : selectedMagTypes) {
                if (node.isKindOf(type)) {
                    useIt = true;
                    break;
                }
            }
            if (useIt && !node.isKindOf("pmag")) {
                magnetNames.add(node.getId());
                Channel bRB = ((Electromagnet) node).getChannel(Electromagnet.FIELD_RB_HANDLE);
                MagnetMainSupply mms = ((Electromagnet) node).getMainSupply();
                Channel bSet = mms.getChannel(MagnetMainSupply.FIELD_SET_HANDLE);
                PVTableCell pvrb;
                if (bRB != null) pvrb = new PVTableCell(bRB); else pvrb = new PVTableCell();
                B_RBs.add(pvrb);
                PVTableCell pvsp;
                if (bSet != null) pvsp = new PVTableCell(bSet); else pvsp = new PVTableCell();
                B_Sets.add(pvsp);
                PVTableCell pvBook;
                try {
                    Channel bBookSet = mms.getChannel(MagnetMainSupply.FIELD_BOOK_HANDLE);
                    pvBook = new PVTableCell(bBookSet);
                } catch (Exception ex) {
                    pvBook = new PVTableCell();
                }
                B_Books.add(pvBook);
                PVTableCell pvtsp;
                if (node.isKindOf("trimmedquad")) {
                    MagnetTrimSupply mts = ((TrimmedQuadrupole) node).getTrimSupply();
                    Channel btSet = mts.getChannel(MagnetTrimSupply.FIELD_SET_HANDLE);
                    if (btSet != null) pvtsp = new PVTableCell(btSet); else pvtsp = new PVTableCell();
                    B_Trim_Sets.add(pvtsp);
                } else {
                    pvtsp = new PVTableCell();
                    B_Trim_Sets.add(pvtsp);
                }
            }
        }
        magnetTableModel.fireTableDataChanged();
    }
