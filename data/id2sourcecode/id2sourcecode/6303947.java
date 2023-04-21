    public Map<Integer, Integer> getChannelLoad() throws RemoteException {
        Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
        for (ChannelWorldInterface cwi : WorldRegistryImpl.getInstance().getAllChannelServers()) ret.put(cwi.getChannelId(), cwi.getConnected());
        return ret;
    }
