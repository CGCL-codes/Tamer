    public JTable makeMPSTable(String MachMode, String mpsFilter, int filterNo, int isNew) {
        String filterSel;
        String chainSel = chainBox.getSelectedItem();
        String subsysSel = (String) subsystemBox.getSelectedItem();
        String deviceSel = (String) deviceBox.getSelectedItem();
        Iterator m1;
        Map.Entry p1;
        if (isNew == 1) {
            if (filterNo == 1) jeriMM = reloadSignals(MachMode, isNew); else if (filterNo == 2) {
                filterSel = (String) subsystemBox.getSelectedItem();
                jeriMM = reloadSubSystems(MachMode, filterSel, isNew);
            } else {
                filterSel = (String) deviceBox.getSelectedItem();
                jeriMM = reloadDevices(MachMode, filterSel, isNew);
            }
            jeriIOC = mainWindow.reloadIOC();
            jeriDevs = mainWindow.getDeviceMap();
            jeriSubSys = mainWindow.getSubSystemMap();
            jeriChanNo = mainWindow.getChannelNoMap();
            isTestedMap = mainWindow.getIsTested_Map();
            PFstatusMap = mainWindow.getPFstatus_Map();
        }
        Iterator keyValue = jeriMM.keySet().iterator();
        while (keyValue.hasNext()) {
            Object key = keyValue.next();
            if (key.toString().indexOf("_Mag:PS_DC") != -1 || key.toString().indexOf("_DCH") != -1 || key.toString().indexOf("_Mag:DCH") != -1) {
                jeriMM.remove(key);
                jeriIOC.remove(key);
                jeriDevs.remove(key);
                jeriSubSys.remove(key);
                jeriChanNo.remove(key);
                isTestedMap.remove(key);
                PFstatusMap.remove(key);
                keyValue = jeriMM.keySet().iterator();
            }
        }
        mpsMMmodel = new MPSmmTableModel(jeriMM, jeriDevs, jeriIOC, jeriChanNo, isTestedMap, PFstatusMap);
        mpsMMmodel.addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                mpsTableModel_tableChanged(e);
            }
        });
        mpsMMtable = clearMPSTable(mpsMMmodel);
        mpsMMtable.setAutoCreateColumnsFromModel(false);
        mpsMMtable.setShowVerticalLines(true);
        mpsMMtable.setShowHorizontalLines(false);
        mpsMMtable.setColumnSelectionAllowed(false);
        mpsMMtable.setSelectionForeground(Color.white);
        mpsMMtable.setSelectionBackground(Color.blue);
        deviceSel = (String) deviceBox.getSelectedItem();
        if (DeviceButton.isSelected() && deviceSel.equals("BLM")) mpsMMtable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); else mpsMMtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mpsMMtable.setRowSelectionAllowed(true);
        mpsMMtable.setVisible(true);
        ListSelectionModel rowSM = mpsMMtable.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                    selectedRow = -1;
                } else {
                    selectedRow = lsm.getMinSelectionIndex();
                }
            }
        });
        mpsMMtable.validate();
        return mpsMMtable;
    }
