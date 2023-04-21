    private void showDifference() {
        xds.addPoint(posData[0], xDataDiff[0]);
        xpds.addPoint(posData[0], xpDataDiff[0]);
        alphaxds.addPoint(posData[0], alphaxDataDiff[0]);
        betaxds.addPoint(posData[0], betaxDataDiff[0]);
        yds.addPoint(posData[0], yDataDiff[0]);
        ypds.addPoint(posData[0], ypDataDiff[0]);
        alphayds.addPoint(posData[0], alphayDataDiff[0]);
        betayds.addPoint(posData[0], betayDataDiff[0]);
        zds.addPoint(posData[0], zDataDiff[0]);
        zpds.addPoint(posData[0], zpDataDiff[0]);
        alphazds.addPoint(posData[0], alphazDataDiff[0]);
        betazds.addPoint(posData[0], betazDataDiff[0]);
        emitxds.addPoint(posData[0], emitxDataDiff[0]);
        emityds.addPoint(posData[0], emityDataDiff[0]);
        emitzds.addPoint(posData[0], emitzDataDiff[0]);
        sigmaxds.addPoint(posData[0], sigmaxDataDiff[0]);
        sigmayds.addPoint(posData[0], sigmayDataDiff[0]);
        sigmazds.addPoint(posData[0], sigmazDataDiff[0]);
        engds.addPoint(posData[0], engDataDiff[0]);
        if (!myMP.getChannelSource().equals(Scenario.SYNC_MODE_DESIGN)) {
            bpmxds.addPoint(bpmPosData[0], bpmxDataDiff[0]);
            bpmyds.addPoint(bpmPosData[0], bpmyDataDiff[0]);
        }
        wsxds.removeAllPoints();
        wsyds.removeAllPoints();
        if (wsxDataDiff != null) {
            wsxds.addPoint(wsPosData[0], wsxDataDiff[0]);
            wsyds.addPoint(wsPosData[0], wsyDataDiff[0]);
        }
    }
