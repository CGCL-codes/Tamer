    public void updatePlot() {
        String strUrl1 = "./twiss.xml";
        EditContext editContext = new EditContext();
        try {
            editContext = getAccelerator().editContext();
            XMLDataManager.readTableGroupFromUrl(editContext, "twiss", strUrl1);
        } catch (XmlDataAdaptor.ParseException e) {
            System.out.println("Exception - " + e.getMessage());
        }
        java.util.List collect = getSelectedSequence().getAllNodes();
        int devCount = collect.size();
        OrTypeQualifier typeQualifier = new OrTypeQualifier();
        typeQualifier.or("QH");
        typeQualifier.or("QV");
        typeQualifier.or("PMQH");
        typeQualifier.or("PMQV");
        typeQualifier.or("DCH");
        typeQualifier.or("DCV");
        java.util.List devices = getSelectedSequence().getNodesWithQualifier(typeQualifier);
        int devCountNoDiag = devices.size();
        double xDatatl[][] = new double[3][devCountNoDiag];
        double xDatatr[][] = new double[3][devCountNoDiag];
        double xDatabl[][] = new double[3][devCountNoDiag];
        double xDatabr[][] = new double[3][devCountNoDiag];
        double yDatatl[][] = new double[3][devCountNoDiag];
        double yDatatr[][] = new double[3][devCountNoDiag];
        double yDatabl[][] = new double[3][devCountNoDiag];
        double yDatabr[][] = new double[3][devCountNoDiag];
        java.util.List collect1 = getSelectedSequence().getAllNodesOfType("BPM");
        ArrayList badBPMs = new ArrayList();
        for (int ii = 0; ii < collect1.size(); ii++) {
            BPM bpm = (BPM) collect1.get(ii);
            if (bpm.getStatus() == false) badBPMs.add(bpm);
        }
        for (int ii = 0; ii < badBPMs.size(); ii++) {
            collect1.remove(badBPMs.get(ii));
        }
        int devBPMCount = collect1.size();
        double xDatabl1[][] = new double[2][devBPMCount];
        double xDatabr1[][] = new double[2][devBPMCount];
        double yDatabl1[][] = new double[2][devBPMCount];
        double yDatabr1[][] = new double[2][devBPMCount];
        double delX[] = new double[devBPMCount];
        double delY[] = new double[devBPMCount];
        int i, j;
        j = 0;
        for (i = 0; i < devCount; i++) {
            AcceleratorNode obj = (AcceleratorNode) collect.get(i);
            if (obj.getType().equals("QH") || obj.getType().equals("QV") || obj.getType().equals("PMQH") || obj.getType().equals("PMQV") || obj.getType().equals("DCH") || obj.getType().equals("DCV")) {
                xDatatl[1][j] = getSelectedSequence().getPosition(obj);
                yDatatl[1][j] = (editContext.getTable("twiss").record("id", obj.getId()).doubleValueForKey("x") - tmpData1[j]);
                tmpData1[j] = editContext.getTable("twiss").record("id", obj.getId()).doubleValueForKey("x");
                xDatabl[1][j] = getSelectedSequence().getPosition(obj);
                yDatabl[1][j] = tmpData1[j];
                xDatatr[1][j] = getSelectedSequence().getPosition(obj);
                yDatatr[1][j] = editContext.getTable("twiss").record("id", obj.getId()).doubleValueForKey("y") - tmpData2[j];
                tmpData2[j] = editContext.getTable("twiss").record("id", obj.getId()).doubleValueForKey("y");
                xDatabr[1][j] = getSelectedSequence().getPosition(obj);
                yDatabr[1][j] = tmpData2[j];
                try {
                    ProbeState probeState = model.stateForElement(obj.getId());
                    TraceXalUnitConverter uc = TraceXalUnitConverter.newConverter(402500000., probeState.getSpeciesRestEnergy(), probeState.getKineticEnergy());
                    xDatatl[2][j] = getSelectedSequence().getPosition(obj);
                    yDatatl[2][j] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getx() - tmpData5[j];
                    tmpData5[j] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getx();
                    xDatabl[2][j] = getSelectedSequence().getPosition(obj);
                    yDatabl[2][j] = tmpData5[j];
                    xDatatr[2][j] = getSelectedSequence().getPosition(obj);
                    yDatatr[2][j] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).gety() - tmpData6[j];
                    tmpData6[j] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).gety();
                    xDatabr[2][j] = getSelectedSequence().getPosition(obj);
                    yDatabr[2][j] = tmpData6[j];
                } catch (ModelException me) {
                    System.out.println(me.getMessage());
                }
                j = j + 1;
            }
        }
        BPM bpmObj[] = new BPM[devBPMCount];
        Channel bpmXCh[] = new Channel[devBPMCount];
        Channel bpmYCh[] = new Channel[devBPMCount];
        double XtempBPMData[] = new double[devBPMCount];
        double YtempBPMData[] = new double[devBPMCount];
        for (int ijk = 0; ijk < devBPMCount; ijk++) {
            XtempBPMData[ijk] = 0.0;
            YtempBPMData[ijk] = 0.0;
            bpmObj[ijk] = (BPM) ((AcceleratorNode) collect1.get(ijk));
            bpmXCh[ijk] = bpmObj[ijk].getChannel(BPM.X_AVG_HANDLE);
            bpmYCh[ijk] = bpmObj[ijk].getChannel(BPM.Y_AVG_HANDLE);
            bpmXCh[ijk].connectAndWait();
            bpmYCh[ijk].connectAndWait();
            Channel.pendIO(5);
        }
        BeamTrigger bt = new BeamTrigger();
        bt.setDelay(1.);
        double Xtemp[][] = new double[devBPMCount][10];
        double Ytemp[][] = new double[devBPMCount][10];
        int k, kk;
        kk = 0;
        for (k = 0; k < 3; k++) {
            if (bt.isOn()) bt.makePulse();
            double beamCurrent = 0.0;
            double bpmSum = 5.0;
            bpmSum = 5.0;
            if (bpmSum > 0.0) {
                for (i = 0; i < devBPMCount; i++) {
                    try {
                        Xtemp[i][kk] = bpmObj[i].getXAvg();
                        Ytemp[i][kk] = bpmObj[i].getYAvg();
                        XtempBPMData[i] = XtempBPMData[i] + Xtemp[i][kk];
                        YtempBPMData[i] = YtempBPMData[i] + Ytemp[i][kk];
                    } catch (ConnectionException e) {
                        System.out.println(e);
                    } catch (GetException e) {
                        System.out.println(e);
                    }
                }
                kk = kk + 1;
            } else {
                System.out.println("Beam Current too low");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("successfully got " + kk + " pulses");
        for (i = 0; i < devBPMCount; i++) {
            XtempBPMData[i] = XtempBPMData[i] / kk;
            YtempBPMData[i] = YtempBPMData[i] / kk;
            delX[i] = 0.0;
            delY[i] = 0.0;
            for (k = 0; k < kk; k++) {
                delX[i] = delX[i] + (XtempBPMData[i] - Xtemp[i][k]) * (XtempBPMData[i] - Xtemp[i][k]);
                delY[i] = delY[i] + (YtempBPMData[i] - Ytemp[i][k]) * (YtempBPMData[i] - Ytemp[i][k]);
            }
            delX[i] = Math.sqrt(delX[i] / kk);
            delY[i] = Math.sqrt(delY[i] / kk);
            System.out.println(bpmObj[i].getId() + "   " + XtempBPMData[i] + "+/-" + delX[i] + "   " + YtempBPMData[i] + "+/-" + delY[i]);
        }
        for (i = 0; i < devBPMCount; i++) {
            AcceleratorNode obJ = (AcceleratorNode) collect1.get(i);
            BPM obj = (BPM) obJ;
            xDatatl[0][i] = getSelectedSequence().getPosition(obj);
            yDatatl[0][i] = XtempBPMData[i] - tmpData3[i];
            tmpData3[i] = XtempBPMData[i];
            xDatabl[0][i] = getSelectedSequence().getPosition(obj);
            xDatabl1[0][i] = xDatabl[0][i];
            xDatabl1[1][i] = xDatabl[0][i];
            yDatabl[0][i] = tmpData3[i];
            yDatabl1[0][i] = delX[i];
            xDatatr[0][i] = getSelectedSequence().getPosition(obj);
            yDatatr[0][i] = YtempBPMData[i] - tmpData4[i];
            tmpData4[i] = YtempBPMData[i];
            xDatabr[0][i] = getSelectedSequence().getPosition(obj);
            xDatabr1[0][i] = xDatabr[0][i];
            xDatabr1[1][i] = xDatabr[0][i];
            yDatabr[0][i] = tmpData4[i];
            yDatabr1[0][i] = delY[i];
        }
        for (i = devBPMCount; i < devCountNoDiag; i++) {
            xDatatl[0][i] = xDatatl[0][devBPMCount - 1];
            yDatatl[0][i] = yDatatl[0][devBPMCount - 1];
            xDatabl[0][i] = xDatabl[0][devBPMCount - 1];
            yDatabl[0][i] = yDatabl[0][devBPMCount - 1];
            xDatatr[0][i] = xDatatr[0][devBPMCount - 1];
            yDatatr[0][i] = yDatatr[0][devBPMCount - 1];
            xDatabr[0][i] = xDatabr[0][devBPMCount - 1];
            yDatabr[0][i] = yDatabr[0][devBPMCount - 1];
        }
        myWindow().plot(xDatatl, yDatatl, xDatatr, yDatatr, xDatabl, yDatabl, xDatabr, yDatabr, delX, delY, devBPMCount);
    }
