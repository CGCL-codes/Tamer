    public void setPV(final String name) {
        if (name != null && !name.equals("")) {
            setChannel(ChannelFactory.defaultFactory().getChannel(name));
        } else {
            setChannel(null);
        }
        _nodeChannelRef = null;
    }
