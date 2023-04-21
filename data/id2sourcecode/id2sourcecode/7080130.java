    public HashMap<String, double[][]> getCavMap() {
        HashMap<String, double[][]> pvMap = new HashMap<String, double[][]>();
        ChannelSnapshot[] css = mss.getChannelSnapshots();
        for (int i = 0; i < css.length; i++) {
            double[] hb0Data, hb1Data;
            if (css[i].getPV().indexOf("HB0") > -1) {
                String hpmId = css[i].getPV().substring(0, 15);
                hb0Data = css[i].getValue();
                double[][] data = new double[2][hb0Data.length];
                if (!pvMap.containsKey(hpmId)) {
                    data[0] = hb0Data;
                    pvMap.put(hpmId, data);
                } else {
                    System.arraycopy(hb0Data, 0, pvMap.get(hpmId)[0], 0, hb0Data.length);
                }
            }
            if (css[i].getPV().indexOf("HB1") > -1) {
                String hpmId = css[i].getPV().substring(0, 15);
                hb1Data = css[i].getValue();
                double[][] data = new double[2][hb1Data.length];
                if (!pvMap.containsKey(hpmId)) {
                    data[1] = hb1Data;
                    pvMap.put(hpmId, data);
                } else {
                    System.arraycopy(hb1Data, 0, pvMap.get(hpmId)[1], 0, hb1Data.length);
                }
            }
        }
        System.out.println("Got " + pvMap.size() + " Cavities.");
        return pvMap;
    }
