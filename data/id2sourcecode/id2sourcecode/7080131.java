    public HashMap<String, Double> getCavMap1() {
        HashMap<String, Double> pvMap = new HashMap<String, Double>();
        ChannelSnapshot[] css = mss.getChannelSnapshots();
        for (int i = 0; i < css.length; i++) {
            double rep;
            if (css[i].getPV().indexOf("EventSelect") > -1) {
                String hpmId = css[i].getPV().substring(0, 16);
                rep = css[i].getValue()[0];
                if (!pvMap.containsKey(hpmId)) {
                    pvMap.put(hpmId, new Double(rep));
                }
            }
        }
        return pvMap;
    }
