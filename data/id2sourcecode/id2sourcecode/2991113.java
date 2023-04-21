    public int getXPos() {
        VGChannel<NodeType, PortType, SignalType> channel = fLayout.getChannel(fCol);
        return channel.getModulesPos();
    }
