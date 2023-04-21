    public void addNewDataPoint(double xValue, Vector<IndependentValue> independVariablesV) {
        if (independVariablesV == null || independVariablesV.size() == 0) {
            addNewDataPoint(xValue);
            return;
        }
        double xValueRB = independVariablesV.lastElement().getValueRB();
        double yValue = 0.0;
        double sigma = 0.0;
        for (int i = 0, nVals = measuredValueV.size(); i < nVals; i++) {
            MeasuredValue mv = (measuredValueV.get(i));
            BasicGraphData gd = mv.getDataContainer(mv.getNumberOfDataContainers() - 1);
            if (gd != null) {
                yValue = mv.getMeasurement();
                sigma = mv.getMeasurementSigma();
                gd.addPoint(xValue, yValue, sigma);
                if (independVariablesV.lastElement().getChannelNameRB() != null) {
                    BasicGraphData gdRB = mv.getDataContainerRB(mv.getNumberOfDataContainersRB() - 1);
                    gdRB.addPoint(xValueRB, yValue, sigma);
                }
            }
        }
    }
