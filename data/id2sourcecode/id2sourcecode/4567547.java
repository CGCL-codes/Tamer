    public Messenger getMessengerImmediate(EndpointAddress addr, Object hint) {
        synchronized (channelCache) {
            Reference<Messenger> existing = channelCache.get(addr);
            if (existing != null) {
                Messenger messenger = existing.get();
                if ((messenger != null) && ((messenger.getState() & Messenger.USABLE) != 0)) {
                    return messenger;
                }
            }
        }
        EndpointAddress plainAddr = new EndpointAddress(addr, null, null);
        Messenger found = theRealThing.getCanonicalMessenger(plainAddr, hint);
        if (found == null) {
            return null;
        }
        ChannelMessenger res = (ChannelMessenger) found.getChannelMessenger(theRealThing.getGroup().getPeerGroupID(), addr.getServiceName(), addr.getServiceParameter());
        synchronized (channelCache) {
            Reference<Messenger> existing = channelCache.get(addr);
            if (existing != null) {
                Messenger messenger = existing.get();
                if ((messenger != null) && ((messenger.getState() & Messenger.USABLE) != 0)) {
                    return messenger;
                }
            }
            res.setMessageWatcher(listenerAdaptor);
            channelCache.put(res.getDestinationAddress(), new WeakReference<Messenger>(res));
        }
        return res;
    }
