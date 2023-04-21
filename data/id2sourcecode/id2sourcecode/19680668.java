    private void showCurrent() {
        xds.addPoint(posData[0], xData[0]);
        xpds.addPoint(posData[0], xpData[0]);
        alphaxds.addPoint(posData[0], alphaxData[0]);
        betaxds.addPoint(posData[0], betaxData[0]);
        yds.addPoint(posData[0], yData[0]);
        ypds.addPoint(posData[0], ypData[0]);
        alphayds.addPoint(posData[0], alphayData[0]);
        betayds.addPoint(posData[0], betayData[0]);
        zds.addPoint(posData[0], zData[0]);
        zpds.addPoint(posData[0], zpData[0]);
        alphazds.addPoint(posData[0], alphazData[0]);
        betazds.addPoint(posData[0], betazData[0]);
        emitxds.addPoint(posData[0], emitxData[0]);
        emityds.addPoint(posData[0], emityData[0]);
        emitzds.addPoint(posData[0], emitzData[0]);
        sigmaxds.addPoint(posData[0], sigmaxData[0]);
        sigmayds.addPoint(posData[0], sigmayData[0]);
        sigmazds.addPoint(posData[0], sigmazData[0]);
        engds.addPoint(posData[0], engData[0]);
        if (!myMP.getChannelSource().equals(Scenario.SYNC_MODE_DESIGN)) {
            bpmxds.addPoint(bpmPosData[0], bpmxData[0]);
            bpmyds.addPoint(bpmPosData[0], bpmyData[0]);
        }
        wsxds.removeAllPoints();
        wsyds.removeAllPoints();
        if (wsxData != null) {
            wsxds.addPoint(wsPosData[0], wsxData[0]);
            wsyds.addPoint(wsPosData[0], wsyData[0]);
        }
    }
