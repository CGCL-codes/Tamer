    public List<CheaterData> getCheaters() throws RemoteException {
        List<CheaterData> allCheaters = new ArrayList<CheaterData>();
        for (int i : WorldRegistryImpl.getInstance().getChannelServer()) {
            ChannelWorldInterface cwi = WorldRegistryImpl.getInstance().getChannel(i);
            try {
                allCheaters.addAll(cwi.getCheaters());
            } catch (RemoteException e) {
                WorldRegistryImpl.getInstance().deregisterChannelServer(i);
            }
        }
        Collections.sort(allCheaters);
        return CollectionUtil.copyFirst(allCheaters, 10);
    }
