    @Override
    protected void customizeCommands(Commander commander) {
        selectProbeAction = new AbstractAction("select-probe") {

            public void actionPerformed(ActionEvent event) {
                File probeMasterFile = mxProxy.getProbeMasterFile();
                probeMasterFile = myWindow().openMasterFileChooser(probeMasterFile);
                if (probeMasterFile == null) return;
                try {
                    mxProxy.setNewProbe(probeMasterFile);
                } catch (Throwable ex) {
                    popThrowableMessage(ex);
                }
            }
        };
        commander.registerAction(selectProbeAction);
        probeEditorAction = new AbstractAction("probe-editor") {

            public void actionPerformed(ActionEvent event) {
                SimpleProbeEditor spe = new SimpleProbeEditor();
                if (mxProxy.hasProbe()) {
                    mxProxy.resetProbe();
                    spe.createSimpleProbeEditor(mxProxy.getProbe());
                } else {
                    if (mxProxy.getProbeMasterFile() != null) {
                        spe.createSimpleProbeEditor(mxProxy.getProbeMasterFile());
                    } else {
                        spe.createSimpleProbeEditor();
                    }
                }
                if (spe.probeHasChanged()) {
                    if (spe.getProbe() instanceof EnvelopeProbe) mxProxy.setNewProbe(spe.getProbe()); else mxProxy.setNewProbe(spe.getProbe());
                }
            }
        };
        commander.registerAction(probeEditorAction);
        runModelAction = new AbstractAction("run-model") {

            public void actionPerformed(ActionEvent event) {
                try {
                    mxProxy.checkAccelerator();
                    mxProxy.checkLattice();
                    mxProxy.checkProbe();
                    mxProxy.synchronizeAcceleratorSeq();
                } catch (LatticeError e) {
                    e.printStackTrace();
                    return;
                }
                if (mxProxy.hasProbe()) {
                    if (usePVLog) {
                        if (pvlogId == 0 && plsc != null) pvlogId = plsc.getPVLogId();
                        if (pvlogId > 0) mxProxy.setPVlogger(pvlogId); else System.out.println("invalid PV Logger ID");
                    }
                    try {
                        mxProxy.runModel();
                    } catch (ModelException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (useCaModel.isSelected() || useRfDesignModel.isSelected()) {
                }
                if (useWsModel.isSelected()) myWindow().plotPane.setWSData(posData, xData, yData);
            }
        };
        commander.registerAction(runModelAction);
        syncModelAction = new AbstractAction("synchronize-model") {

            public void actionPerformed(ActionEvent event) {
                try {
                    mxProxy.checkAccelerator();
                    mxProxy.checkLattice();
                    mxProxy.synchronizeAcceleratorSeq();
                } catch (LatticeError e) {
                    e.printStackTrace();
                    return;
                }
            }
        };
        commander.registerAction(syncModelAction);
        pvloggerAction = new AbstractAction("save-pvlogger") {

            public void actionPerformed(ActionEvent event) {
                loggerSession.publishSnapshot(snapshot);
            }
        };
        commander.registerAction(pvloggerAction);
        pvloggerAction.setEnabled(false);
        latticeTreeAction = new AbstractAction("lattice-tree") {

            public void actionPerformed(ActionEvent event) {
                System.out.println("start document prep...");
                Document latticeDoc = mxProxy.getOnLineModelLatticeAsDocument();
                System.out.println("end document prep...");
                myWindow().setLatticeTree(latticeDoc);
                myWindow().getTabbedPane().setEnabledAt(2, true);
            }
        };
        commander.registerAction(latticeTreeAction);
        latticeTreeAction.setEnabled(false);
        usePvlogModel.setSelected(MPXMain.PARAM_SRC == ModelProxy.PARAMSRC_DESIGN);
        usePvlogModel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (pvLogSelector == null) pvLogSelector = plsc.choosePVLogId(); else pvLogSelector.setVisible(true);
                mxProxy.setChannelSource(ModelProxy.PARAMSRC_DESIGN);
                MPXMain.PARAM_SRC = mxProxy.getChannelSource();
                syncModelAction.setEnabled(true);
                usePVLog = true;
            }
        });
        commander.registerModel("use-pvlogger", usePvlogModel);
        useWsModel.setSelected(MPXMain.PARAM_SRC == ModelProxy.PARAMSRC_DESIGN);
        useWsModel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String currentDirectory = _wireFileTracker.getRecentFolderPath();
                JFrame frame = new JFrame();
                JFileChooser fileChooser = new JFileChooser(currentDirectory);
                fileChooser.addChoosableFileFilter(new WireFileFilter());
                int status = fileChooser.showOpenDialog(frame);
                if (status == JFileChooser.APPROVE_OPTION) {
                    _wireFileTracker.cacheURL(fileChooser.getSelectedFile());
                    File file = fileChooser.getSelectedFile();
                    setWSFile(file);
                }
                mxProxy.setChannelSource(ModelProxy.PARAMSRC_DESIGN);
                MPXMain.PARAM_SRC = mxProxy.getChannelSource();
                syncModelAction.setEnabled(true);
                usePVLog = true;
            }
        });
        commander.registerModel("use-ws", useWsModel);
        useCaModel.setSelected(MPXMain.PARAM_SRC == ModelProxy.PARAMSRC_LIVE);
        useCaModel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                useCa(event);
                initPVLogger();
                pvloggerAction.setEnabled(true);
            }
        });
        commander.registerModel("use-ca", useCaModel);
        useDesignModel = new ToggleButtonModel();
        useDesignModel.setSelected(MPXMain.PARAM_SRC == ModelProxy.PARAMSRC_DESIGN);
        useDesignModel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                useDesign(event);
                pvloggerAction.setEnabled(false);
            }
        });
        commander.registerModel("use-design", useDesignModel);
        useRfDesignModel.setSelected(MPXMain.PARAM_SRC == ModelProxy.PARAMSRC_RF_DESIGN);
        useRfDesignModel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                useRfDesign(event);
            }
        });
        commander.registerModel("use-rf_design", useRfDesignModel);
        set.setSelected(false);
        set.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                isReadback = false;
                if (getSelectedSequence() != null) {
                    List allEMMag = selectedSequence.getAllNodesOfType("emag");
                    for (int i = 0; i < allEMMag.size(); i++) {
                        ((Electromagnet) allEMMag.get(i)).setUseFieldReadback(false);
                    }
                } else {
                    myWindow().getTextField().setText("Please select Accelerator/Sequence first!");
                }
            }
        });
        commander.registerModel("set", set);
        readback.setSelected(true);
        readback.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                isReadback = true;
                if (selectedSequence != null) {
                    List allEMMag = selectedSequence.getAllNodesOfType("emag");
                    for (int i = 0; i < allEMMag.size(); i++) {
                        ((Electromagnet) allEMMag.get(i)).setUseFieldReadback(true);
                    }
                } else {
                    myWindow().getTextField().setText("Please select Accelerator/Sequence first!");
                }
            }
        });
        commander.registerModel("readback", readback);
    }
