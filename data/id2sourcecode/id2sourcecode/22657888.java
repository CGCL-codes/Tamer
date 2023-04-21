    public String currentChannel() throws RemoteException {
        int chan = tzapThread.currentChannel();
        if (chan == -1) return new String("Nothing");
        return new String(confParser.getChannel(chan).channelName());
    }
