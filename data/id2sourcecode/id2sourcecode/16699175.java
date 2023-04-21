    public void messengerChat(int messengerid, String chattext, String namefrom) throws RemoteException {
        MapleMessenger messenger = WorldRegistryImpl.getInstance().getMessenger(messengerid);
        if (messenger == null) {
            throw new IllegalArgumentException("No messenger with the specified messengerid exists");
        }
        for (int i : WorldRegistryImpl.getInstance().getChannelServer()) {
            ChannelWorldInterface cwi = WorldRegistryImpl.getInstance().getChannel(i);
            try {
                cwi.messengerChat(messenger, chattext, namefrom);
            } catch (RemoteException e) {
                WorldRegistryImpl.getInstance().deregisterChannelServer(i);
            }
        }
    }
