    public boolean isConnected(String charName) throws RemoteException {
        for (int i : WorldRegistryImpl.getInstance().getChannelServer()) {
            ChannelWorldInterface cwi = WorldRegistryImpl.getInstance().getChannel(i);
            try {
                if (cwi.isConnected(charName)) {
                    return true;
                }
            } catch (RemoteException e) {
                WorldRegistryImpl.getInstance().deregisterChannelServer(i);
            }
        }
        return false;
    }
