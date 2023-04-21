    public void start() {
        ChannelFactory cf = ChannelFactory.defaultFactory();
        machineModeChannel = cf.getChannel(machineModeName);
        machineModeChannel.addConnectionListener(this);
        machineModeChannel.requestConnection();
        for (String bcmName : bcmChannelNames.keySet()) {
            Channel ch = cf.getChannel(bcmChannelNames.get(bcmName));
            ch.addConnectionListener(this);
            ch.requestConnection();
            bcmChannels.put(bcmName, ch);
            bcmMonitors.put(ch, null);
        }
    }
