    public void removeConnection(TextureNodeConnection c) {
        if (c == null) return;
        if (allConnections.remove(c)) {
            c.source.parent.getChannel().removeChannelChangeListener(c);
            c.target.parent.getChannel().setInputChannel(c.target.channelIndex, null);
        } else {
            System.err.println("ERROR in removeConnection: got invalid connection " + c);
        }
    }
