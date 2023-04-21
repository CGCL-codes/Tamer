    public void sendSpouseChat(String sender, String target, String message) throws RemoteException {
        for (int i : WorldRegistryImpl.getInstance().getChannelServer()) {
            ChannelWorldInterface cwi = WorldRegistryImpl.getInstance().getChannel(i);
            try {
                cwi.sendSpouseChat(sender, target, message);
            } catch (Exception e) {
                WorldRegistryImpl.getInstance().deregisterChannelServer(i);
            }
        }
    }
