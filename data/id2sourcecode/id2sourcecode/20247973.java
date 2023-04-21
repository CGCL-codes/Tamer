    public Channel getChannel() throws RemoteException {
        if (ch == null) {
            ch = ep.getChannel();
        }
        return ch;
    }
