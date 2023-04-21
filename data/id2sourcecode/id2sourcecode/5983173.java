    public boolean isSelected() {
        for (int i = 0; i < getNumberOfChannelSelections(); i++) {
            if (getChannelSelection(i).isSelected()) {
                return true;
            }
        }
        return false;
    }
