    private void leaveCurrentChannel() {
        if (getStore().getChannel() == null) {
            throw new NullPointerException("null channel bean");
        }
        ChannelBean bean = getStore().getChannel();
    }
