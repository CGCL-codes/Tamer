    public void updatePlotData(MPXProxy mp) {
        if (xData != null) {
            diff.setEnabled(true);
            xDataDiff = new double[1][xData[0].length];
            xpDataDiff = new double[1][xData[0].length];
            alphaxDataDiff = new double[1][xData[0].length];
            betaxDataDiff = new double[1][xData[0].length];
            yDataDiff = new double[1][xData[0].length];
            ypDataDiff = new double[1][xData[0].length];
            alphayDataDiff = new double[1][xData[0].length];
            betayDataDiff = new double[1][xData[0].length];
            zDataDiff = new double[1][xData[0].length];
            zpDataDiff = new double[1][xData[0].length];
            alphazDataDiff = new double[1][xData[0].length];
            betazDataDiff = new double[1][xData[0].length];
            emitxDataDiff = new double[1][xData[0].length];
            emityDataDiff = new double[1][xData[0].length];
            emitzDataDiff = new double[1][xData[0].length];
            sigmaxDataDiff = new double[1][xData[0].length];
            sigmayDataDiff = new double[1][xData[0].length];
            sigmazDataDiff = new double[1][xData[0].length];
            engDataDiff = new double[1][xData[0].length];
            xDataOld = new double[1][xData[0].length];
            xpDataOld = new double[1][xData[0].length];
            alphaxDataOld = new double[1][xData[0].length];
            betaxDataOld = new double[1][xData[0].length];
            yDataOld = new double[1][xData[0].length];
            ypDataOld = new double[1][xData[0].length];
            alphayDataOld = new double[1][xData[0].length];
            betayDataOld = new double[1][xData[0].length];
            zDataOld = new double[1][xData[0].length];
            zpDataOld = new double[1][xData[0].length];
            alphazDataOld = new double[1][xData[0].length];
            betazDataOld = new double[1][xData[0].length];
            emitxDataOld = new double[1][xData[0].length];
            emityDataOld = new double[1][xData[0].length];
            emitzDataOld = new double[1][xData[0].length];
            sigmaxDataOld = new double[1][xData[0].length];
            sigmayDataOld = new double[1][xData[0].length];
            sigmazDataOld = new double[1][xData[0].length];
            engDataOld = new double[1][xData[0].length];
            System.arraycopy(xData[0], 0, xDataOld[0], 0, xData[0].length);
            System.arraycopy(xpData[0], 0, xpDataOld[0], 0, xpData[0].length);
            System.arraycopy(yData[0], 0, yDataOld[0], 0, yData[0].length);
            System.arraycopy(ypData[0], 0, ypDataOld[0], 0, ypData[0].length);
            System.arraycopy(zData[0], 0, zDataOld[0], 0, zData[0].length);
            System.arraycopy(zpData[0], 0, zpDataOld[0], 0, zpData[0].length);
            System.arraycopy(alphaxData[0], 0, alphaxDataOld[0], 0, alphaxData[0].length);
            System.arraycopy(betaxData[0], 0, betaxDataOld[0], 0, betaxData[0].length);
            System.arraycopy(alphayData[0], 0, alphayDataOld[0], 0, alphayData[0].length);
            System.arraycopy(betayData[0], 0, betayDataOld[0], 0, betayData[0].length);
            System.arraycopy(alphazData[0], 0, alphazDataOld[0], 0, alphazData[0].length);
            System.arraycopy(betazData[0], 0, betazDataOld[0], 0, betazData[0].length);
            System.arraycopy(emitxData[0], 0, emitxDataOld[0], 0, emitxData[0].length);
            System.arraycopy(emityData[0], 0, emityDataOld[0], 0, emityData[0].length);
            System.arraycopy(emitzData[0], 0, emitzDataOld[0], 0, emitzData[0].length);
            System.arraycopy(sigmaxData[0], 0, sigmaxDataOld[0], 0, sigmaxData[0].length);
            System.arraycopy(sigmayData[0], 0, sigmayDataOld[0], 0, sigmayData[0].length);
            System.arraycopy(sigmazData[0], 0, sigmazDataOld[0], 0, sigmazData[0].length);
            System.arraycopy(engData[0], 0, engDataOld[0], 0, engData[0].length);
        }
        AcceleratorSeq accSeq = mp.getAcceleratorSequence();
        OrTypeQualifier otq = new OrTypeQualifier();
        otq.or("Bnch");
        otq.or("SCLCavity");
        NotTypeQualifier ntq = new NotTypeQualifier(otq);
        java.util.List<AcceleratorNode> allNodes = accSeq.getAllNodesWithQualifier(ntq);
        allNodes = AcceleratorSeq.filterNodesByStatus(allNodes, true);
        elementCount = allNodes.size();
        myMP = mp;
        int probeType = mp.getProbeType();
        ArrayList states = new ArrayList();
        if (probeType == ModelProxy.ENVELOPE_PROBE) {
            showAlphaX.setEnabled(true);
            showBetaX.setEnabled(true);
            showAlphaY.setEnabled(true);
            showBetaY.setEnabled(true);
            showAlphaZ.setEnabled(true);
            showBetaZ.setEnabled(true);
            showEmitX.setEnabled(true);
            showEmitY.setEnabled(true);
            showEmitZ.setEnabled(true);
            showSigmaX.setEnabled(true);
            showSigmaY.setEnabled(true);
            showSigmaZ.setEnabled(true);
            showEng.setEnabled(true);
        }
        if (probeType == ModelProxy.PARTICLE_PROBE) {
            showAlphaX.setEnabled(false);
            showBetaX.setEnabled(false);
            showAlphaY.setEnabled(false);
            showBetaY.setEnabled(false);
            showAlphaZ.setEnabled(false);
            showBetaZ.setEnabled(false);
            showEmitX.setEnabled(false);
            showEmitY.setEnabled(false);
            showEmitZ.setEnabled(false);
            showSigmaX.setEnabled(false);
            showSigmaY.setEnabled(false);
            showSigmaZ.setEnabled(false);
            showEng.setEnabled(true);
        }
        if (probeType == ModelProxy.TRANSFERMAP_PROBE) {
            showAlphaX.setEnabled(true);
            showBetaX.setEnabled(true);
            showAlphaY.setEnabled(true);
            showBetaY.setEnabled(true);
            showAlphaZ.setEnabled(false);
            showBetaZ.setEnabled(false);
            showEmitX.setEnabled(false);
            showEmitY.setEnabled(false);
            showEmitZ.setEnabled(false);
            showSigmaX.setEnabled(false);
            showSigmaY.setEnabled(false);
            showSigmaZ.setEnabled(false);
            showEng.setEnabled(false);
        }
        if (mp.getChannelSource().equals(Scenario.SYNC_MODE_LIVE) || mp.getChannelSource().equals(Scenario.SYNC_MODE_RF_DESIGN)) {
            showBpmX.setEnabled(true);
            showBpmY.setEnabled(true);
            showWsX.setEnabled(true);
            showWsY.setEnabled(true);
            bpmData = new BpmData(mp.getAcceleratorSequence(), mp);
            double[][] tmpXData = bpmData.getXData();
            double[][] tmpYData = bpmData.getYData();
            bpmPosData = new double[1][tmpXData[0].length];
            bpmxData = new double[1][tmpXData[0].length];
            bpmyData = new double[1][tmpXData[0].length];
            bpmxDataDiff = new double[1][tmpXData[0].length];
            bpmyDataDiff = new double[1][tmpXData[0].length];
            for (int i = 0; i < tmpXData[0].length; i++) {
                bpmPosData[0][i] = tmpXData[0][i];
                bpmxData[0][i] = tmpXData[1][i];
                bpmyData[0][i] = tmpYData[1][i];
                if (bpmxDataOld != null) {
                    if (bpmxDataOld[0].length == bpmxData[0].length) {
                        bpmxDataDiff[0][i] = bpmxData[0][i] - bpmxDataOld[0][i];
                        bpmyDataDiff[0][i] = bpmyData[0][i] - bpmyDataOld[0][i];
                    }
                }
            }
            bpmxDataOld = new double[1][tmpXData[0].length];
            bpmyDataOld = new double[1][tmpXData[0].length];
            System.arraycopy(bpmxData[0], 0, bpmxDataOld[0], 0, bpmxData[0].length);
            System.arraycopy(bpmyData[0], 0, bpmyDataOld[0], 0, bpmyData[0].length);
            wsData = new WsData(mp.getAcceleratorSequence(), mp);
            double[][] tmpXData1 = wsData.getXData();
            double[][] tmpYData1 = wsData.getYData();
            wsPosData = new double[1][tmpXData1[0].length];
            wsxData = new double[1][tmpXData1[0].length];
            wsyData = new double[1][tmpXData1[0].length];
            wsxDataDiff = new double[1][tmpXData1[0].length];
            wsyDataDiff = new double[1][tmpXData1[0].length];
            for (int i = 0; i < tmpXData1[0].length; i++) {
                wsPosData[0][i] = tmpXData1[0][i];
                wsxData[0][i] = tmpXData1[1][i];
                wsyData[0][i] = tmpYData1[1][i];
                if (wsxDataOld != null) {
                    wsxDataDiff[0][i] = wsxData[0][i] - wsxDataOld[0][i];
                    wsyDataDiff[0][i] = wsyData[0][i] - wsyDataOld[0][i];
                }
            }
            wsxDataOld = new double[1][tmpXData1[0].length];
            wsyDataOld = new double[1][tmpXData1[0].length];
            System.arraycopy(wsxData[0], 0, wsxDataOld[0], 0, wsxData[0].length);
            System.arraycopy(wsyData[0], 0, wsyDataOld[0], 0, wsyData[0].length);
        } else if (myWindow.mpxDocument.useWsModel.isSelected()) {
            showWsX.setEnabled(true);
            showWsY.setEnabled(true);
        } else {
            showBpmX.setEnabled(false);
            showBpmY.setEnabled(false);
            showWsX.setEnabled(false);
            showWsY.setEnabled(false);
        }
        posData = new double[1][elementCount];
        xData = new double[1][elementCount];
        xpData = new double[1][elementCount];
        alphaxData = new double[1][elementCount];
        betaxData = new double[1][elementCount];
        yData = new double[1][elementCount];
        ypData = new double[1][elementCount];
        alphayData = new double[1][elementCount];
        betayData = new double[1][elementCount];
        zData = new double[1][elementCount];
        zpData = new double[1][elementCount];
        alphazData = new double[1][elementCount];
        betazData = new double[1][elementCount];
        emitxData = new double[1][elementCount];
        emityData = new double[1][elementCount];
        emitzData = new double[1][elementCount];
        sigmaxData = new double[1][elementCount];
        sigmayData = new double[1][elementCount];
        sigmazData = new double[1][elementCount];
        engData = new double[1][elementCount];
        if (elementCount > 0) {
            elementIds = new String[elementCount];
            if (probeType == ModelProxy.ENVELOPE_PROBE) {
                for (int i = 0; i < elementCount; i++) {
                    try {
                        ProbeState probeState = mp.stateForElement((allNodes.get(i)).getId());
                        uc = TraceXalUnitConverter.newConverter(BeamConstants.FREQUENCY, probeState.getSpeciesRestEnergy(), probeState.getKineticEnergy());
                        elementIds[i] = probeState.getElementId();
                        posData[0][i] = probeState.getPosition();
                        xData[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getx();
                        xpData[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getxp();
                        Twiss[] twiss = ((EnvelopeProbeState) probeState).twissParameters();
                        alphaxData[0][i] = uc.xalToTraceTransverse(twiss[0]).getAlpha();
                        betaxData[0][i] = uc.xalToTraceTransverse(twiss[0]).getBeta();
                        yData[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).gety();
                        ypData[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getyp();
                        alphayData[0][i] = uc.xalToTraceTransverse(twiss[1]).getAlpha();
                        betayData[0][i] = uc.xalToTraceTransverse(twiss[1]).getBeta();
                        zData[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getz();
                        zpData[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getzp();
                        alphazData[0][i] = uc.xalToTraceLongitudinal(twiss[2]).getAlpha();
                        betazData[0][i] = uc.xalToTraceLongitudinal(twiss[2]).getBeta();
                        emitxData[0][i] = uc.xalToTraceTransverse(twiss[0]).getEmittance();
                        emityData[0][i] = uc.xalToTraceTransverse(twiss[1]).getEmittance();
                        emitzData[0][i] = uc.xalToTraceLongitudinal(twiss[2]).getEmittance();
                        sigmaxData[0][i] = twiss[0].getEnvelopeRadius() * 1000.;
                        sigmayData[0][i] = twiss[1].getEnvelopeRadius() * 1000.;
                        sigmazData[0][i] = twiss[2].getEnvelopeRadius() * 1000.;
                        engData[0][i] = ((EnvelopeProbeState) probeState).getKineticEnergy() / 1.e6;
                        if (xDataOld != null && probeType == oldProbeType) {
                            xDataDiff[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getx() - xDataOld[0][i];
                            xpDataDiff[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getxp() - xpDataOld[0][i];
                            alphaxDataDiff[0][i] = uc.xalToTraceTransverse(twiss[0]).getAlpha() - alphaxDataOld[0][i];
                            betaxDataDiff[0][i] = uc.xalToTraceTransverse(twiss[0]).getBeta() - betaxDataOld[0][i];
                            yDataDiff[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).gety() - yDataOld[0][i];
                            ypDataDiff[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getyp() - ypDataOld[0][i];
                            alphayDataDiff[0][i] = uc.xalToTraceTransverse(twiss[1]).getAlpha() - alphayDataOld[0][i];
                            betayDataDiff[0][i] = uc.xalToTraceTransverse(twiss[1]).getBeta() - betayDataOld[0][i];
                            zDataDiff[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getz() - zDataOld[0][i];
                            zpDataDiff[0][i] = uc.xalToTraceCoordinates(((EnvelopeProbeState) probeState).phaseMean()).getzp() - zpDataOld[0][i];
                            alphazDataDiff[0][i] = uc.xalToTraceLongitudinal(twiss[2]).getAlpha() - alphazDataOld[0][i];
                            betazDataDiff[0][i] = uc.xalToTraceLongitudinal(twiss[2]).getBeta() - betazDataOld[0][i];
                            emitxDataDiff[0][i] = uc.xalToTraceTransverse(twiss[0]).getEmittance() - emitxDataOld[0][i];
                            emityDataDiff[0][i] = uc.xalToTraceTransverse(twiss[1]).getEmittance() - emityDataOld[0][i];
                            emitzDataDiff[0][i] = uc.xalToTraceLongitudinal(twiss[2]).getEmittance() - emitzDataOld[0][i];
                            sigmaxDataDiff[0][i] = twiss[0].getEnvelopeRadius() * 1000. - sigmaxDataOld[0][i];
                            sigmayDataDiff[0][i] = twiss[1].getEnvelopeRadius() * 1000. - sigmayDataOld[0][i];
                            sigmazDataDiff[0][i] = twiss[2].getEnvelopeRadius() * 1000. - sigmazDataOld[0][i];
                            engDataDiff[0][i] = ((EnvelopeProbeState) probeState).getKineticEnergy() / 1.e6 - engDataOld[0][i];
                        } else {
                            oldProbeType = probeType;
                        }
                    } catch (ModelException me) {
                        System.out.println(me.getMessage());
                    }
                }
            }
            if (probeType == ModelProxy.TRANSFERMAP_PROBE) {
                for (int i = 0; i < elementCount; i++) {
                    try {
                        ProbeState probeState = mp.stateForElement((allNodes.get(i)).getId());
                        uc = TraceXalUnitConverter.newConverter(BeamConstants.FREQUENCY, probeState.getSpeciesRestEnergy(), probeState.getKineticEnergy());
                        elementIds[i] = probeState.getElementId();
                        posData[0][i] = probeState.getPosition();
                        xData[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).getFixedOrbit()).getx();
                        xpData[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).getFixedOrbit()).getxp();
                        alphaxData[0][i] = ((TransferMapState) probeState).getTwiss()[0].getAlpha();
                        betaxData[0][i] = ((TransferMapState) probeState).getTwiss()[0].getBeta();
                        yData[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).getFixedOrbit()).gety();
                        ypData[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).getFixedOrbit()).getyp();
                        alphayData[0][i] = ((TransferMapState) probeState).getTwiss()[1].getAlpha();
                        betayData[0][i] = ((TransferMapState) probeState).getTwiss()[1].getBeta();
                        zData[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).phaseCoordinates()).getz();
                        zpData[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).phaseCoordinates()).getzp();
                        alphazData[0][i] = ((TransferMapState) probeState).getTwiss()[2].getAlpha();
                        betazData[0][i] = ((TransferMapState) probeState).getTwiss()[2].getBeta();
                        emitxData[0][i] = ((TransferMapState) probeState).getTwiss()[0].getEmittance();
                        emityData[0][i] = ((TransferMapState) probeState).getTwiss()[1].getEmittance();
                        emitzData[0][i] = ((TransferMapState) probeState).getTwiss()[2].getEmittance();
                        if (xDataOld != null && probeType == oldProbeType) {
                            xDataDiff[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).getFixedOrbit()).getx() - xDataOld[0][i];
                            xpDataDiff[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).getFixedOrbit()).getxp() - xpDataOld[0][i];
                            alphaxDataDiff[0][i] = uc.xalToTraceTransverse(((TransferMapState) probeState).getTwiss()[0]).getAlpha() - alphaxDataOld[0][i];
                            betaxDataDiff[0][i] = uc.xalToTraceTransverse(((TransferMapState) probeState).getTwiss()[0]).getBeta() - betaxDataOld[0][i];
                            yDataDiff[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).getFixedOrbit()).gety() - yDataOld[0][i];
                            ypDataDiff[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).getFixedOrbit()).getyp() - ypDataOld[0][i];
                            alphayDataDiff[0][i] = uc.xalToTraceTransverse(((TransferMapState) probeState).getTwiss()[1]).getAlpha() - alphayDataOld[0][i];
                            betayDataDiff[0][i] = uc.xalToTraceTransverse(((TransferMapState) probeState).getTwiss()[1]).getBeta() - betayDataOld[0][i];
                            zDataDiff[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).phaseCoordinates()).getz() - zDataOld[0][i];
                            zpDataDiff[0][i] = uc.xalToTraceCoordinates(((TransferMapState) probeState).phaseCoordinates()).getzp() - zpDataOld[0][i];
                            alphazDataDiff[0][i] = uc.xalToTraceLongitudinal(((TransferMapState) probeState).getTwiss()[2]).getAlpha() - alphazDataOld[0][i];
                            betazDataDiff[0][i] = uc.xalToTraceLongitudinal(((TransferMapState) probeState).getTwiss()[2]).getBeta() - betazDataOld[0][i];
                            emitxDataDiff[0][i] = uc.xalToTraceTransverse(((TransferMapState) probeState).getTwiss()[0]).getEmittance() - emitxDataOld[0][i];
                            emityDataDiff[0][i] = uc.xalToTraceTransverse(((TransferMapState) probeState).getTwiss()[1]).getEmittance() - emityDataOld[0][i];
                            emitzDataDiff[0][i] = uc.xalToTraceLongitudinal(((TransferMapState) probeState).getTwiss()[2]).getEmittance() - emitzDataOld[0][i];
                        } else {
                            oldProbeType = probeType;
                        }
                    } catch (ModelException me) {
                        System.out.println(me.getMessage());
                    }
                }
            }
            if (probeType == ModelProxy.PARTICLE_PROBE) {
                for (int i = 0; i < elementCount; i++) {
                    try {
                        ProbeState probeState = mp.stateForElement((allNodes.get(i)).getId());
                        uc = TraceXalUnitConverter.newConverter(402500000., probeState.getSpeciesRestEnergy(), probeState.getKineticEnergy());
                        elementIds[i] = probeState.getElementId();
                        posData[0][i] = probeState.getPosition();
                        xData[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).getx();
                        xpData[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).getxp();
                        yData[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).gety();
                        ypData[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).getyp();
                        zData[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).getz();
                        zpData[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).getzp();
                        engData[0][i] = ((ParticleProbeState) probeState).getKineticEnergy() / 1.e6;
                        if (xDataOld != null && probeType == oldProbeType) {
                            xDataDiff[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).getx() - xDataOld[0][i];
                            xpDataDiff[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).getxp() - xpDataOld[0][i];
                            yDataDiff[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).gety() - yDataOld[0][i];
                            ypDataDiff[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).getyp() - ypDataOld[0][i];
                            zDataDiff[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).getz() - zDataOld[0][i];
                            zpDataDiff[0][i] = uc.xalToTraceCoordinates(((ParticleProbeState) probeState).phaseCoordinates()).getzp() - zpDataOld[0][i];
                            engDataDiff[0][i] = ((ParticleProbeState) probeState).getKineticEnergy() / 1.e6 - engDataOld[0][i];
                        } else {
                            oldProbeType = probeType;
                        }
                    } catch (ModelException me) {
                        System.out.println(me.getMessage());
                    }
                }
            }
        }
        chart.removeAllGraphData();
        current.setSelected(true);
        showCurrent();
    }
