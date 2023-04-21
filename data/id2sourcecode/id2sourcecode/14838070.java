    public void copy(int sourceIndex, int destIndex, int length) {
        for (int i = 0; i < getChannelCount(); i++) {
            copy(i, sourceIndex, destIndex, length);
        }
    }
