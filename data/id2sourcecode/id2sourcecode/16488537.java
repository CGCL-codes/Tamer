    void updateLut() {
        IndexColorModel cm = (IndexColorModel) imp.getChannelProcessor().getColorModel();
        if (mapSize == 0) return;
        cm.getReds(reds);
        cm.getGreens(greens);
        cm.getBlues(blues);
        for (int i = 0; i < mapSize; i++) c[i] = new Color(reds[i] & 255, greens[i] & 255, blues[i] & 255);
    }
