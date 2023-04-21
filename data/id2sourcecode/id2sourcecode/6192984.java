    public HashMap<String, double[][]> getBPMMap() {
        HashMap<String, double[][]> pvMap = new HashMap<String, double[][]>();
        ChannelSnapshot[] css = mss.getChannelSnapshots();
        for (int i = 0; i < css.length; i++) {
            double[] xdata, ydata;
            if (css[i].getPV().indexOf("xTBT") > -1) {
                String BPMId = css[i].getPV().substring(0, 17);
                xdata = css[i].getValue();
                double[][] data = new double[2][xdata.length];
                if (!pvMap.containsKey(BPMId)) {
                    data[0] = xdata;
                    pvMap.put(BPMId, data);
                } else {
                    System.arraycopy(xdata, 0, pvMap.get(BPMId)[0], 0, xdata.length);
                }
            }
            if (css[i].getPV().indexOf("yTBT") > -1) {
                String BPMId = css[i].getPV().substring(0, 17);
                ydata = css[i].getValue();
                double[][] data = new double[2][ydata.length];
                if (!pvMap.containsKey(BPMId)) {
                    data[1] = ydata;
                    pvMap.put(BPMId, data);
                } else {
                    System.arraycopy(ydata, 0, pvMap.get(BPMId)[1], 0, ydata.length);
                }
            }
        }
        System.out.println("Got " + pvMap.size() + " BPMs.");
        return pvMap;
    }
