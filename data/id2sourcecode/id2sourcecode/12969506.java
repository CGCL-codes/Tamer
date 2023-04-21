    public void setChannels(final Element channels) throws ClassNotFoundException, JeeManagementException {
        this.l_channels.clear();
        this.l_channels.addAll(ComponentsDigester.getChannels(channels));
    }
