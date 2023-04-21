    public SwitcherToSimulatedH0() {
        Border border = BorderFactory.createEtchedBorder();
        switcherBorder = BorderFactory.createTitledBorder(border, "Switcher to H0 Simmulation Mode");
        switcherPanel.setBorder(switcherBorder);
        updateCtrl.setUpdateTime(0.5);
        liveDataRecords = liveDataTableModel.getRecords();
        memorizedRecords = memorizedDataTableModel.getRecords();
        liveDataTable = new JTable(liveDataTableModel);
        memorizedDataTable = new JTable(memorizedDataTableModel);
        JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel upperInsidePanel = new JPanel(new GridLayout(1, 2, 1, 1));
        liveDataBorder = BorderFactory.createTitledBorder(border, "Live Data");
        memorizedDataBorder = BorderFactory.createTitledBorder(border, "Memorized Data");
        JPanel liveDataPanel = new JPanel(new BorderLayout());
        liveDataPanel.setBorder(liveDataBorder);
        JScrollPane scrollpane1 = new JScrollPane(liveDataTable);
        liveDataPanel.add(scrollpane1, BorderLayout.CENTER);
        JPanel memorizedDataPanel = new JPanel(new BorderLayout());
        memorizedDataPanel.setBorder(memorizedDataBorder);
        JPanel memButtonPanel = new JPanel(new GridLayout(2, 1, 1, 1));
        memButtonPanel.add(memorizeButton);
        memButtonPanel.add(restoreButton);
        JPanel memButton_0_Panel = new JPanel(new BorderLayout());
        memButton_0_Panel.add(memButtonPanel, BorderLayout.NORTH);
        JPanel memButton_1_Panel = new JPanel(new BorderLayout());
        memButton_1_Panel.add(memButton_0_Panel, BorderLayout.WEST);
        JPanel memButton_2_Panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
        memButton_2_Panel.add(memButton_1_Panel);
        memorizedDataPanel.add(memButton_2_Panel, BorderLayout.WEST);
        JScrollPane scrollpane2 = new JScrollPane(memorizedDataTable);
        memorizedDataPanel.add(scrollpane2, BorderLayout.CENTER);
        upperInsidePanel.add(liveDataPanel);
        upperInsidePanel.add(memorizedDataPanel);
        upperPanel.add(upperInsidePanel, BorderLayout.CENTER);
        JPanel lowerPanel = new JPanel(new BorderLayout());
        transformBorder = BorderFactory.createTitledBorder(border, "Magnet's Current Settings");
        lowerPanel.setBorder(transformBorder);
        JPanel coeffPanel = new JPanel(new GridLayout(3, 2, 1, 1));
        coeffPanel.add(injSpt_coeff_Label);
        coeffPanel.add(injSpt_coeff_TextField);
        coeffPanel.add(dha11_coeff_Label);
        coeffPanel.add(dha11_coeff_TextField);
        coeffPanel.add(dha12_coeff_Label);
        coeffPanel.add(dha12_coeff_TextField);
        JPanel lower_0_Panel = new JPanel(new BorderLayout());
        lower_0_Panel.add(coeffPanel, BorderLayout.CENTER);
        lower_0_Panel.add(transformButton, BorderLayout.SOUTH);
        lowerPanel.add(lower_0_Panel, BorderLayout.WEST);
        switcherPanel.add(upperPanel, BorderLayout.NORTH);
        switcherPanel.add(lowerPanel, BorderLayout.CENTER);
        injSpt_coeff_TextField.setDecimalFormat(new DecimalFormat("######.######"));
        dha11_coeff_TextField.setDecimalFormat(new DecimalFormat("######.######"));
        dha12_coeff_TextField.setDecimalFormat(new DecimalFormat("######.######"));
        injSpt_coeff_TextField.setValue(1.01474);
        dha11_coeff_TextField.setValue(0.742995);
        dha12_coeff_TextField.setValue(20.0);
        for (int i = 0; i < 9; i++) {
            wrpV.add(new WrappedChannel());
        }
        wrpV.get(0).setChannelNameQuietly("HEBT_Mag:PS_InjSptm:I_Set");
        wrpV.get(1).setChannelNameQuietly("Ring_Mag:PS_DH_A11:I_Set");
        wrpV.get(2).setChannelNameQuietly("Ring_Mag:PS_DH_A12:I_Set");
        wrpV.get(3).setChannelNameQuietly("HEBT_Mag:InjSptm:B");
        wrpV.get(4).setChannelNameQuietly("Ring_Mag:PS_DH_A11:B");
        wrpV.get(5).setChannelNameQuietly("Ring_Mag:PS_DH_A12:B");
        wrpV.get(6).setChannelNameQuietly("HEBT_Mag:PS_InjSptm:B_Book");
        wrpV.get(7).setChannelNameQuietly("Ring_Mag:PS_DH_A11:B_Book");
        wrpV.get(8).setChannelNameQuietly("Ring_Mag:PS_DH_A12:B_Book");
        Iterator<WrappedChannel> iter = wrpV.iterator();
        while (iter.hasNext()) {
            WrappedChannel wch = iter.next();
            liveDataTableModel.addTableRecord(wch.getChannelName());
            memorizedDataTableModel.addTableRecord(wch.getChannelName());
        }
        ActionListener pvListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateCtrl.update();
            }
        };
        iter = wrpV.iterator();
        while (iter.hasNext()) {
            WrappedChannel wch = iter.next();
            wch.addStateListener(pvListener);
            wch.addValueListener(pvListener);
        }
        ActionListener updateListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Iterator<WrappedChannel> iter_inner = wrpV.iterator();
                while (iter_inner.hasNext()) {
                    WrappedChannel wch = iter_inner.next();
                    TableRecord liveTR = liveDataTableModel.getRecord(wch.getChannelName());
                    if (wch.isGood()) {
                        liveTR.setValue(wch.getValue());
                        liveTR.setStatus(true);
                    } else {
                        liveTR.setValue(0.);
                        liveTR.setStatus(false);
                        messageTextLocal.setText("Cannot read PV: " + wch.getChannelName());
                    }
                }
                liveDataTableModel.fireTableDataChanged();
            }
        };
        updateCtrl.addActionListener(updateListener);
        memorizeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Iterator<WrappedChannel> iter_inner = wrpV.iterator();
                while (iter_inner.hasNext()) {
                    WrappedChannel wch = iter_inner.next();
                    TableRecord memTR = memorizedDataTableModel.getRecord(wch.getChannelName());
                    if (wch.isGood()) {
                        memTR.setValue(wch.getValue());
                        memTR.setStatus(true);
                    } else {
                        memTR.setValue(0.);
                        memTR.setStatus(false);
                        messageTextLocal.setText("Cannot read PV: " + wch.getChannelName());
                    }
                }
                memorizedDataTableModel.fireTableDataChanged();
            }
        });
        restoreButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int[] inds = { 6, 7, 8, 0, 1, 2 };
                for (int i = 0; i < inds.length; i++) {
                    int ind = inds[i];
                    WrappedChannel wch = wrpV.get(ind);
                    TableRecord memTR = memorizedDataTableModel.getRecord(wch.getChannelName());
                    if (memTR.getStatus()) {
                        wch.setValue(memTR.getValue());
                    }
                }
            }
        });
        transformButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                WrappedChannel wchI = wrpV.get(0);
                WrappedChannel wchB = wrpV.get(3);
                WrappedChannel wchB_Book = wrpV.get(6);
                if (wchB.isGood()) {
                    double coeff = wchB.getValue() / wchI.getValue();
                    double valI = wchI.getValue() * injSpt_coeff_TextField.getValue();
                    double valB_Book = coeff * valI;
                    wchI.setValue(valI);
                    wchB_Book.setValue(valB_Book);
                } else {
                    messageTextLocal.setText("Cannot read PV: " + wchB.getChannelName());
                }
                wchI = wrpV.get(1);
                wchB = wrpV.get(4);
                wchB_Book = wrpV.get(7);
                if (wchB.isGood()) {
                    double coeff = wchB.getValue() / wchI.getValue();
                    double valI = wchI.getValue() * dha11_coeff_TextField.getValue();
                    double valB_Book = coeff * valI;
                    wchI.setValue(valI);
                    wchB_Book.setValue(valB_Book);
                } else {
                    messageTextLocal.setText("Cannot read PV: " + wchB.getChannelName());
                }
                wchI = wrpV.get(2);
                wchB = wrpV.get(5);
                wchB_Book = wrpV.get(8);
                if (wchB.isGood()) {
                    double coeff = wchB.getValue() / wchI.getValue();
                    double valI = dha12_coeff_TextField.getValue();
                    double valB_Book = coeff * valI;
                    wchI.setValue(valI);
                    wchB_Book.setValue(valB_Book);
                } else {
                    messageTextLocal.setText("Cannot read PV: " + wchB.getChannelName());
                }
            }
        });
    }
