    public void makeNewSetOfMeasurements(Vector<IndependentValue> independVariablesV) {
        IndependentValue indVal = null;
        if (independVariablesV != null && independVariablesV.size() > 0) {
            indVal = independVariablesV.lastElement();
        }
        for (int i = 0, nVals = measuredValueV.size(); i < nVals; i++) {
            MeasuredValue mv = (measuredValueV.get(i));
            mv.createNewDataContainer();
            BasicGraphData gd = mv.getDataContainer(mv.getNumberOfDataContainers() - 1);
            BasicGraphData gdRB = null;
            if (indVal != null) {
                if (indVal.getChannelNameRB() != null) {
                    mv.createNewDataContainerRB();
                    gdRB = mv.getDataContainerRB(mv.getNumberOfDataContainersRB() - 1);
                }
            }
            if (independVariablesV == null) continue;
            for (int j = 0, nPar = independVariablesV.size(); j < nPar; j++) {
                IndependentValue indVal_tmp = (independVariablesV.get(j));
                if (indVal_tmp != null && indVal_tmp.getChannelName() != null) {
                    gd.setGraphProperty(indVal_tmp.getChannelName(), new Double(indVal_tmp.getCurrentValue()));
                }
                if (indVal_tmp != null && indVal_tmp.getChannelNameRB() != null) {
                    gd.setGraphProperty(indVal_tmp.getChannelNameRB(), new Double(indVal_tmp.getCurrentValueRB()));
                }
                if (gdRB != null) {
                    if (indVal_tmp != null && indVal_tmp.getChannelName() != null) {
                        gdRB.setGraphProperty(indVal_tmp.getChannelName(), new Double(indVal_tmp.getCurrentValue()));
                    }
                    if (indVal_tmp != null && indVal_tmp.getChannelNameRB() != null) {
                        gdRB.setGraphProperty(indVal_tmp.getChannelNameRB(), new Double(indVal_tmp.getCurrentValueRB()));
                    }
                }
            }
        }
        if (newSetOfMeasurementsListener != null) {
            newSetOfMeasurementsListener.actionPerformed(newSetOfMeasurementsEvent);
        }
    }
