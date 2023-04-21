    public int getHighestSelectedIndex() {
        int max = 0;
        for (int i = 0; i < getNumberOfChannelSelections(); i++) {
            AChannelSelection chs = getChannelSelection(i);
            int l = chs.getOffset() + chs.getLength();
            if (l > max) {
                max = l;
            }
        }
        return max;
    }
