    public int getMaxLength() {
        int max = 0;
        for (int i = 0; i < getNumberOfChannelSelections(); i++) {
            int l = getChannelSelection(i).getLength();
            if (l > max) {
                max = l;
            }
        }
        return max;
    }
