    public boolean addConnection(TextureNodeConnection c) {
        if (checkForCycle(c.source, c.target)) {
            System.out.println("WARNING: cycles not allowed!");
            return false;
        }
        TextureNodeConnection inputConnection = getConnectionAtInputPoint(c.target);
        if (inputConnection != null) removeConnection(inputConnection);
        c.target.parent.getChannel().setInputChannel(c.target.channelIndex, c.source.parent.getChannel());
        c.source.parent.getChannel().addChannelChangeListener(c);
        allConnections.add(c);
        return true;
    }
