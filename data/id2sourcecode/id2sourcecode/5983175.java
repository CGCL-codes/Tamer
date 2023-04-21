    public int getLowestSelectedIndex() {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < getNumberOfChannelSelections(); i++) {
            int l = getChannelSelection(i).getOffset();
            if (l < min) {
                min = l;
            }
        }
        return min;
    }
