    protected void setChannelCount(int count) {
        if (count == getChannelCount()) return;
        if (count < getChannelCount()) {
            for (int ch = getChannelCount() - 1; ch > count - 1; ch--) {
                removeChannel(ch);
            }
        } else {
            while (getChannelCount() < count) {
                addChannel(false);
            }
        }
    }
