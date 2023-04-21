    public Map<Integer, Integer> getConnected() throws RemoteException {
        Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
        int total = 0;
        for (int i : WorldRegistryImpl.getInstance().getChannelServer()) {
            ChannelWorldInterface cwi = WorldRegistryImpl.getInstance().getChannel(i);
            try {
                int curConnected = cwi.getConnected();
                ret.put(i, curConnected);
                total += curConnected;
            } catch (RemoteException e) {
                WorldRegistryImpl.getInstance().deregisterChannelServer(i);
            }
        }
        ret.put(0, total);
        return ret;
    }
