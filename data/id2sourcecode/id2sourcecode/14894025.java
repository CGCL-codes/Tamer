    public int getChannel() {
        cc2420.updateActiveFrequency();
        return cc2420.getActiveChannel();
    }
