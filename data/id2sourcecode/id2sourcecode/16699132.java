    public void serverReady() throws RemoteException {
        ready = true;
        for (LoginWorldInterface wli : WorldRegistryImpl.getInstance().getLoginServer()) {
            try {
                wli.channelOnline(cb.getChannelId(), cb.getIP());
            } catch (RemoteException e) {
                WorldRegistryImpl.getInstance().deregisterLoginServer(wli);
            }
        }
        log.info("Channel {} is online.", cb.getChannelId());
    }
