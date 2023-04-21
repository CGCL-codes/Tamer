    public QuadshakerDocument() {
        super();
        updatingController.setUpdateTime(1.0);
        shakerController = new ShakerController(updatingController);
        shakerController.setTableModels(quadsTable, bpmsTable);
        shakeAnalysis = new ShakeAnalysis();
        shakeAnalysis.setTableModels(quadsTable, bpmsTable);
        shakerController.getShakerRunController().setShakeAnalysis(shakeAnalysis);
        orbitCorrector = new OrbitCorrector();
        orbitCorrector.setTableModel(quadsTable);
        shakeAnalysis.setOrbitCorrector(orbitCorrector);
        firstPanel = shakerController.getPanel();
        secondPanel = shakeAnalysis.getPanel();
        thirdPanel = orbitCorrector.getPanel();
        mainTabbedPanel.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                JTabbedPane tbp = (JTabbedPane) e.getSource();
                setActivePanel(tbp.getSelectedIndex());
            }
        });
        makePreferencesPanel();
        mainTabbedPanel.add("Shaker", firstPanel);
        mainTabbedPanel.add("Analysis", secondPanel);
        mainTabbedPanel.add("Orbit Correction", thirdPanel);
        mainTabbedPanel.add("Preferences", preferencesPanel);
        mainTabbedPanel.setSelectedIndex(0);
        loadDefaultAccelerator();
        Accelerator acc = getAccelerator();
        AcceleratorSeq seq1 = acc.getSequence("CCL1");
        AcceleratorSeq seq2 = acc.getSequence("CCL2");
        AcceleratorSeq seq3 = acc.getSequence("CCL3");
        AcceleratorSeq seq4 = acc.getSequence("CCL4");
        ArrayList cclArr = new ArrayList();
        cclArr.add(seq1);
        cclArr.add(seq2);
        cclArr.add(seq3);
        cclArr.add(seq4);
        AcceleratorSeqCombo cclSeq = new AcceleratorSeqCombo("CCL", cclArr);
        java.util.List cclQuads = cclSeq.getAllNodesWithQualifier((new OrTypeQualifier()).or(Quadrupole.s_strType));
        Iterator itr = cclQuads.iterator();
        while (itr.hasNext()) {
            Quadrupole quad = (Quadrupole) itr.next();
            Quad_Element quadElm = new Quad_Element(quad.getId());
            quadElm.setActive(true);
            quadsTable.getListModel().addElement(quadElm);
            quadElm.getWrpChRBField().setChannelName(quad.getChannel("fieldRB").channelName());
            if (quad.getType().equals("QTH") || quad.getType().equals("QTV")) {
                quadElm.isItTrim(true);
                quadElm.getWrpChCurrent().setChannelName(quad.getChannel("trimI_Set").channelName());
            } else {
                quadElm.isItTrim(false);
                quadElm.getWrpChCurrent().setChannelName(quad.getChannel("I_Set").channelName());
            }
        }
        AcceleratorSeq seq5 = acc.getSequence("SCLMed");
        java.util.List cclBPMs = cclSeq.getAllNodesOfType("BPM");
        java.util.List sclBPMs = seq5.getAllNodesOfType("BPM");
        int n_add = Math.min(4, sclBPMs.size());
        for (int i = 0; i < n_add; i++) {
            cclBPMs.add(sclBPMs.get(i));
        }
        for (int i = 0, n = cclBPMs.size(); i < n; i++) {
            BPM bpm = (BPM) cclBPMs.get(i);
            BPM_Element bpmElm = new BPM_Element(bpm.getId());
            bpmElm.setActive(true);
            bpmElm.getWrpChannelX().setChannelName(bpm.getChannel("xAvg").channelName());
            bpmElm.getWrpChannelY().setChannelName(bpm.getChannel("yAvg").channelName());
            bpmsTable.getListModel().addElement(bpmElm);
        }
        AcceleratorSeq seq0 = acc.getSequence("DTL6");
        ArrayList dtl_cclArr = new ArrayList();
        dtl_cclArr.add(seq0);
        dtl_cclArr.add(seq1);
        dtl_cclArr.add(seq2);
        dtl_cclArr.add(seq3);
        dtl_cclArr.add(seq4);
        AcceleratorSeqCombo comboSeq1 = new AcceleratorSeqCombo("DTL-CCL", dtl_cclArr);
        orbitCorrector.setAccelSeq(comboSeq1);
        ArrayList dtl_sclArr = new ArrayList();
        dtl_sclArr.add(seq0);
        dtl_sclArr.add(seq1);
        dtl_sclArr.add(seq2);
        dtl_sclArr.add(seq3);
        dtl_sclArr.add(seq4);
        dtl_sclArr.add(seq5);
        AcceleratorSeqCombo comboSeq2 = new AcceleratorSeqCombo("DTL-SCL", dtl_sclArr);
        shakeAnalysis.setAccelSeq(comboSeq2);
    }
