    public WorldLocation getLocation(String charName) throws RemoteException {
        for (int i : WorldRegistryImpl.getInstance().getChannelServer()) {
            ChannelWorldInterface cwi = WorldRegistryImpl.getInstance().getChannel(i);
            try {
                if (cwi.isConnected(charName)) {
                    return new WorldLocation(cwi.getLocation(charName), cwi.getChannelId());
                }
            } catch (RemoteException e) {
                WorldRegistryImpl.getInstance().deregisterChannelServer(i);
            }
        }
        return null;
    }
