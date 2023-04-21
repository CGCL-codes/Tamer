    private void startAction() {
        clearAll();
        if (tableChanged) {
            tableChanged = false;
        }
        theDoc.resetWireDataMap();
        selectWires();
        String[] theId = new String[theDoc.selectedWires.size()];
        if (firstScan) {
            thePM = new ProfileMonitor[theDoc.selectedWires.size()];
            nSteps = new int[theDoc.selectedWires.size()];
            posUpdate = new PVUpdaterDbl[theDoc.selectedWires.size()];
            progProdder = new ProgProdder[theDoc.selectedWires.size()];
            wirePanels = new WirePanel[theDoc.selectedWires.size()];
            for (int i = 0; i < theDoc.selectedWires.size(); i++) {
                System.out.println("selectedwires size " + theDoc.selectedWires.size());
                theId[i] = ((AcceleratorNode) theDoc.selectedWires.get(i)).getId();
                thePM[i] = (ProfileMonitor) theDoc.selectedWires.get(i);
                connectArrays(thePM[i]);
                wirePanels[i] = new WirePanel(thePM[i], theDoc, this);
                theDoc.wireDataMap.put(theId[i], new WireData(wirePanels[i]));
                panelList.add(wirePanels[i]);
            }
            doPostSigmaTable();
            firstScan = false;
        } else {
        }
        killAllThreads = new ThreadKiller(this);
        xDataUpdate = new PVUpdaterDblArry[theDoc.selectedWires.size()];
        yDataUpdate = new PVUpdaterDblArry[theDoc.selectedWires.size()];
        zDataUpdate = new PVUpdaterDblArry[theDoc.selectedWires.size()];
        for (int i = 0; i < theDoc.selectedWires.size(); i++) {
            try {
                nSteps[i] = thePM[i].getNSteps();
            } catch (ConnectionException ce) {
                System.err.println(ce.getMessage());
                ce.printStackTrace();
            } catch (GetException ge) {
                System.err.println(ge.getMessage());
                ge.printStackTrace();
            }
            theTabbedPane.add(theId[i], wirePanels[i]);
            try {
                tableModel.setMaxValueAt(thePM[i].getScanLength(), ((Integer) selectedRows.get(i)).intValue());
            } catch (ConnectionException ce) {
                System.err.println(ce.getMessage());
                ce.printStackTrace();
            } catch (GetException ge) {
                System.err.println(ge.getMessage());
                ge.printStackTrace();
            }
            xDataUpdate[i] = new PVUpdaterDblArry(wirePanels[i].pm.getChannel(ProfileMonitor.V_REAL_DATA_HANDLE), theId[i], theDoc, 0, wirePanels[i].pm.PosC, wirePanels[i]);
            yDataUpdate[i] = new PVUpdaterDblArry(wirePanels[i].pm.getChannel(ProfileMonitor.D_REAL_DATA_HANDLE), theId[i], theDoc, 1, wirePanels[i].pm.PosC, wirePanels[i]);
            zDataUpdate[i] = new PVUpdaterDblArry(wirePanels[i].pm.getChannel(ProfileMonitor.H_REAL_DATA_HANDLE), theId[i], theDoc, 2, wirePanels[i].pm.PosC, wirePanels[i]);
            posUpdate[i] = new PVUpdaterDbl(thePM[i].PosC);
            progProdder[i] = new ProgProdder((JProgressBar) tableModel.getValueAt(((Integer) selectedRows.get(i)).intValue(), 2), posUpdate[i], tableModel);
            if (theDoc.series == Boolean.TRUE) {
                seriesList.add(theDoc.selectedWires.get(i));
            }
            if (theDoc.series == Boolean.FALSE) {
                int timeout = 0;
                while (timeout <= 10) {
                    if (wirePanels[i].status.getText().startsWith("Ready")) {
                        break;
                    }
                    if (wirePanels[i].status.getText().startsWith("Scan")) {
                        break;
                    }
                    if (wirePanels[i].status.getText().startsWith("Found")) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        System.out.println("Sleep interrupted for wirescanner " + theId[i] + " while checking if ready");
                        System.err.println(ie.getMessage());
                        ie.printStackTrace();
                    }
                    timeout++;
                }
                if (timeout <= 10) {
                    try {
                        msgField.setText("Scanning...");
                        thePM[i].doScan();
                    } catch (ConnectionException ce) {
                        System.err.println(ce.getMessage());
                        ce.printStackTrace();
                    } catch (PutException pe) {
                        System.err.println(pe.getMessage());
                        pe.printStackTrace();
                    }
                    startTime = new Date();
                } else {
                    System.out.println("Timeout expired for " + theId[i] + " while waiting for ready");
                    JOptionPane.showMessageDialog(this, "Timeout expired while waiting for ready", theId[i], JOptionPane.ERROR_MESSAGE);
                }
            }
            progProdder[i].start();
            wirePanels[i].chartProdder.start();
            theTabbedPane.setVisible(true);
        }
        if (theDoc.series == Boolean.TRUE) {
            seriesRun = new SeriesRun(seriesList, panelList);
            seriesRun.start();
            startTime = new Date();
        }
        killAllThreads.start();
        Channel.flushIO();
    }
