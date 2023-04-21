    public boolean hasTokens(int n) {
        if ((write - read) >= n) {
            return true;
        } else {
            FifoManager.getInstance().addEmptyFifo(this);
            return false;
        }
    }
