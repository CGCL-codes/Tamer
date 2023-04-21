    public void addChannelRemoveListener(final String chnl, final RemoveListener listener) {
        if (!listeningToConnect) {
            getBayeux().getChannel(Bayeux.META_SUBSCRIBE, true).subscribe(serviceClient);
            serviceClient.addListener(new RemovalForwardingListener());
            listeningToConnect = true;
        }
        removalListeners.put("/" + chnl, listener);
    }
