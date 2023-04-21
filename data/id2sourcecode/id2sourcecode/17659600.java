    private JTable getJTable() {
        if (cavTable == null) {
            int rfCavSize = cavs.size();
            String[] columnNames = { "Cavity", "Rep Rate", "Rep Rate (logged)", "Field", "Field (logged)" };
            cellData = new String[rfCavSize][1];
            homNames = new String[rfCavSize];
            String repPV;
            String cavVPV;
            tableModel = new RFTableModel(columnNames, rfCavSize);
            repChs = new Channel[cavs.size()];
            cavVChs = new Channel[cavs.size()];
            Iterator it = cavs.iterator();
            int i = 0;
            while (it.hasNext()) {
                cellData[i][0] = ((SCLCavity) it.next()).getId();
                tableModel.addRowName(cellData[i][0], i);
                homNames[i] = cellData[i][0].replace("RF:Cav", "LLRF:HPM");
                repPV = (cellData[i][0].replace("RF:Cav", "LLRF:Gate")).concat("_LOCAL:EventSelect");
                repChs[i] = ChannelFactory.defaultFactory().getChannel(repPV);
                repChs[i].addConnectionListener(this);
                InputPVTableCell pvCell1 = new InputPVTableCell(repChs[i], i, 1);
                tableModel.addPVCell(pvCell1, i, 1);
                getChannelVec(repChs[i]).add(pvCell1);
                cavVPV = (cellData[i][0].replace("RF:Cav", "LLRF:FCM")).concat(":cavV");
                cavVChs[i] = ChannelFactory.defaultFactory().getChannel(cavVPV);
                cavVChs[i].addConnectionListener(this);
                InputPVTableCell pvCell2 = new InputPVTableCell(cavVChs[i], i, 3);
                tableModel.addPVCell(pvCell2, i, 3);
                getChannelVec(cavVChs[i]).add(pvCell2);
                i++;
            }
            cavTable = new JTable(tableModel);
            cavTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            cavTable.getColumnModel().getColumn(0).setPreferredWidth(120);
            DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
            dtcr.setForeground(Color.red);
            cavTable.getColumnModel().getColumn(2).setCellRenderer(dtcr);
            cavTable.getColumnModel().getColumn(4).setCellRenderer(dtcr);
            ListSelectionModel rowSM = cavTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) return;
                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (lsm.isSelectionEmpty()) {
                    } else {
                        int selectedRow = lsm.getMinSelectionIndex();
                        theHOM = selectedRow;
                        updatePlot(homNames[selectedRow]);
                        System.out.println(cavTable.getValueAt(selectedRow, 0) + " is selected.");
                    }
                }
            });
            final TableProdder prodder = new TableProdder(tableModel);
            prodder.start();
            connectAll();
        }
        return cavTable;
    }
