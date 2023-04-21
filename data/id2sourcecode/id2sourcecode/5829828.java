    protected void buildGUI() {
        if (static_pr == null) {
            static_pr = new PropertyArray();
            static_pr.text = prText;
            static_pr.textName = prTextName;
            static_pr.bool = prBool;
            static_pr.boolName = prBoolName;
        }
        if (static_presets == null) {
            static_presets = new Presets(getClass(), static_pr.toProperties(true));
        }
        presets = static_presets;
        pr = (PropertyArray) static_pr.clone();
        batchVector = new ArrayList();
        new BatchObjectArray();
        GridBagConstraints con;
        JButton ggCmd;
        JCheckBox ggVisible, ggConsole;
        JScrollPane ggBatchPane, ggParamPane;
        gui = new GUISupport();
        con = gui.getGridBagConstraints();
        con.insets = new Insets(1, 2, 1, 2);
        final BatchDlg enc_this = this;
        cbo = new ClipboardOwner() {

            public void lostOwnership(Clipboard clipboard, Transferable contents) {
            }
        };
        procL = new ProcessorListener() {

            public void processorStarted(ProcessorEvent e) {
                teleAction();
            }

            public void processorStopped(ProcessorEvent e) {
                teleAction();
            }

            public void processorPaused(ProcessorEvent e) {
                teleAction();
            }

            public void processorResumed(ProcessorEvent e) {
                teleAction();
            }

            public void processorProgress(ProcessorEvent e) {
                teleAction();
            }

            private void teleAction() {
                synchronized (enc_this) {
                    enc_this.notify();
                }
            }
        };
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ID = gui.getItemID(e);
                int i, j, k;
                int[] rows;
                BatchObject[] dup;
                Transferable t;
                BatchObject bObj;
                switch(ID) {
                    case GG_CMDOPEN:
                        if (batchTable.getSelectedRowCount() != 1) break;
                        bObj = (BatchObject) batchVector.get(batchTable.getSelectedRow());
                        if (bObj.command != BatchObject.CMD_MODULE) break;
                        DocumentFrame procWin;
                        try {
                            procWin = getProcInstance(bObj, null);
                            procWin.fillGUI();
                            procWin.setVisible(true);
                        } catch (Exception e1) {
                            GUIUtil.displayError(getComponent(), e1, getTitle());
                        }
                        break;
                    case GG_CMDADD:
                        i = batchTable.getSelectedRow() + 1;
                        i = i == 0 ? batchVector.size() : i;
                        batchTable.clearSelection();
                        batchVector.add(i, new BatchObject());
                        batchTM.fireTableRowsInserted(i, i);
                        batchTable.setRowSelectionInterval(i, i);
                        break;
                    case GG_CMDCUT:
                    case GG_CMDCOPY:
                        rows = batchTable.getSelectedRows();
                        if (rows.length > 0) {
                            dup = new BatchObject[rows.length];
                            for (i = 0; i < rows.length; i++) {
                                dup[i] = new BatchObject((BatchObject) batchVector.get(rows[i]));
                            }
                            AbstractApplication.getApplication().getClipboard().setContents(new BatchObjectArray(dup), cbo);
                            if (ID == GG_CMDCUT) {
                                for (boolean finished = false; !finished; ) {
                                    for (i = 0, j = -1, k = -1; i < rows.length; i++) {
                                        if (rows[i] > j) {
                                            j = rows[i];
                                            k = i;
                                        }
                                    }
                                    if (j >= 0) {
                                        batchVector.remove(j);
                                        rows[k] = -1;
                                        batchTM.fireTableRowsDeleted(j, j);
                                    } else {
                                        finished = true;
                                    }
                                }
                            }
                        }
                        break;
                    case GG_CMDPASTE:
                        i = batchTable.getSelectedRow() + 1;
                        i = i == 0 ? batchVector.size() : i;
                        try {
                            t = AbstractApplication.getApplication().getClipboard().getContents(enc_this);
                            if (t != null) {
                                if (t.isDataFlavorSupported(BatchObjectArray.flavor)) {
                                    dup = (BatchObject[]) t.getTransferData(BatchObjectArray.flavor);
                                    if (dup.length > 0) {
                                        batchTable.clearSelection();
                                        for (j = 0, k = i; j < dup.length; j++, k++) {
                                            batchVector.add(k, dup[j]);
                                        }
                                        batchTM.fireTableRowsInserted(i, k - 1);
                                        batchTable.setRowSelectionInterval(i, k - 1);
                                    }
                                }
                            }
                        } catch (IllegalStateException e97) {
                        } catch (IOException e98) {
                        } catch (UnsupportedFlavorException e99) {
                        }
                        break;
                }
            }
        };
        tml = new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                int i, k;
                BatchObject bObj;
                if (e.getSource() == batchTM) {
                    if (e.getType() == TableModelEvent.DELETE) return;
                    k = e.getFirstRow();
                    if (k >= 0 && !batchVector.isEmpty()) {
                        bObj = (BatchObject) batchVector.get(k);
                        if ((bObj.command == BatchObject.CMD_MODULE) && (bObj.modObj.modClass == null)) {
                            final DocumentHandler dh = AbstractApplication.getApplication().getDocumentHandler();
                            final DocumentFrame[] modules = new DocumentFrame[dh.getDocumentCount()];
                            for (int m = 0; m < modules.length; m++) {
                                modules[m] = ((Session) dh.getDocument(m)).getFrame();
                            }
                            String[] winNames;
                            ListDlg dlg;
                            PropertyArray pa;
                            String str;
                            int winNum = modules.length;
                            List v;
                            for (i = 0; i < winNum; i++) {
                                str = modules[i].getClass().getName();
                                str = str.substring(str.lastIndexOf('.') + 1);
                                if ((modules[i] == enc_this) || Util.isValueInArray(str, EXCLUDE_DLG)) {
                                    winNum--;
                                    for (int j = i; j < winNum; j++) {
                                        modules[j] = modules[j + 1];
                                    }
                                    i--;
                                    continue;
                                }
                            }
                            winNames = new String[winNum];
                            for (i = 0; i < winNum; i++) {
                                winNames[i] = modules[i].getTitle();
                            }
                            if (winNum > 1) {
                                dlg = new ListDlg(getWindow(), "Choose Module", winNames);
                                i = dlg.getList();
                            } else {
                                i = winNum - 1;
                            }
                            if (i < 0) return;
                            bObj.modObj.name = winNames[i];
                            ((DocumentFrame) modules[i]).fillPropertyArray();
                            pa = ((DocumentFrame) modules[i]).getPropertyArray();
                            bObj.modObj.prParam = pa.toProperties(true);
                            bObj.modObj.modClass = modules[i].getClass().getName();
                            v = new ArrayList();
                            for (int j = 0; j < pa.text.length; j++) {
                                if (pa.textName[j].indexOf("File") >= 0) {
                                    v.add(new Integer(j));
                                }
                            }
                            bObj.modObj.modParam = new String[v.size()][2];
                            for (int j = 0; j < v.size(); j++) {
                                bObj.modObj.modParam[j][0] = pa.textName[j];
                                bObj.modObj.modParam[j][1] = pa.text[j];
                            }
                            batchTM.fireTableRowsUpdated(k, k);
                            updateParamTable();
                        }
                    }
                } else if (e.getSource() == paramTM) {
                }
            }
        };
        con.fill = GridBagConstraints.BOTH;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addLabel(new GroupLabel("Batch List", GroupLabel.ORIENT_HORIZONTAL, GroupLabel.BRACE_NONE));
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.weightx = 1.0;
        con.weighty = 1.0;
        initBatchTable();
        gui.registerGadget(batchTable, GG_BATCH);
        ggBatchPane = new JScrollPane(batchTable);
        gui.addGadget(ggBatchPane, GG_BATCHPANE);
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridwidth = 1;
        con.weightx = 0.05;
        con.weighty = 0.0;
        final JToolBar tb = new JToolBar();
        tb.setBorderPainted(false);
        tb.setFloatable(false);
        ggCmd = new JButton(" Add ");
        gui.registerGadget(ggCmd, GG_CMDADD);
        tb.add(ggCmd);
        ggCmd.addActionListener(al);
        ggCmd = new JButton(" Cut ");
        gui.registerGadget(ggCmd, GG_CMDCUT);
        tb.add(ggCmd);
        ggCmd.addActionListener(al);
        ggCmd = new JButton(" Copy ");
        gui.registerGadget(ggCmd, GG_CMDCOPY);
        tb.add(ggCmd);
        ggCmd.addActionListener(al);
        ggCmd = new JButton(" Paste ");
        gui.registerGadget(ggCmd, GG_CMDPASTE);
        tb.add(ggCmd);
        ggCmd.addActionListener(al);
        ggCmd = new JButton(" Open ");
        gui.registerGadget(ggCmd, GG_CMDOPEN);
        tb.add(ggCmd);
        ggCmd.addActionListener(al);
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addGadget(tb, GG_OFF_OTHER + 666);
        con.fill = GridBagConstraints.BOTH;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addLabel(new GroupLabel("Module Parameter Settings", GroupLabel.ORIENT_HORIZONTAL, GroupLabel.BRACE_NONE));
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.weightx = 1.0;
        con.weighty = 0.3;
        initParamTable();
        gui.registerGadget(paramTable, GG_PARAMS);
        ggParamPane = new JScrollPane(paramTable);
        gui.addGadget(ggParamPane, GG_PARAMSPANE);
        con.weighty = 0.0;
        con.fill = GridBagConstraints.BOTH;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addLabel(new GroupLabel("Control Settings", GroupLabel.ORIENT_HORIZONTAL, GroupLabel.BRACE_NONE));
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridwidth = 1;
        ggVisible = new JCheckBox("Visible modules");
        gui.addCheckbox(ggVisible, GG_VISIBLE, null);
        con.gridwidth = GridBagConstraints.REMAINDER;
        ggConsole = new JCheckBox("Console output");
        gui.addCheckbox(ggConsole, GG_CONSOLE, null);
        setPreferredSize(new Dimension(500, 600));
        initGUI(this, FLAGS_PRESETS | FLAGS_PROGBAR | FLAGS_PROGBARASYNC, gui);
    }
