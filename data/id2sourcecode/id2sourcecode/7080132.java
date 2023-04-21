    public HashMap<String, Double> getCavMap2() {
        HashMap<String, Double> pvMap = new HashMap<String, Double>();
        ChannelSnapshot[] css = mss.getChannelSnapshots();
        for (int i = 0; i < css.length; i++) {
            double rep;
            if (css[i].getPV().indexOf("cavV") > -1) {
                String hpmId = css[i].getPV().substring(0, 15);
                rep = css[i].getValue()[0];
                if (!pvMap.containsKey(hpmId)) {
                    pvMap.put(hpmId, new Double(rep));
                }
            }
        }
        return pvMap;
    }
