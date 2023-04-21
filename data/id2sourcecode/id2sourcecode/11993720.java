    public HashMap<String, Double> getBPMPhaseMap() {
        HashMap<String, Double> bpmYMap = new HashMap<String, Double>();
        ChannelSnapshot[] css = mss.getChannelSnapshots();
        for (int i = 0; i < css.length; i++) {
            if (css[i].getPV().indexOf(":phaseAvg") > -1) {
                double[] val = css[i].getValue();
                bpmYMap.put(css[i].getPV(), new Double(val[0]));
            }
        }
        return bpmYMap;
    }
