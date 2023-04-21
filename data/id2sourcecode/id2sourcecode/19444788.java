    public void addIrcChannels() {
        try {
            IrcSpec spec = ServiceManager.getManager().getMainFrame().getIrcSpec();
            for (int i = 0; i < spec.numChannels(); i++) {
                org.furthurnet.xmlparser.IrcChannel channel = spec.getChannel(i);
                addIrcChannel(channel.getChannel(), channel.getDescription());
            }
        } catch (Exception e) {
        }
    }
