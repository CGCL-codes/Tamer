    protected void updateItunes() throws StoreException {
        if (filesAdded.size() == 0 && filesDeleted.size() == 0 && filesUpdated.size() == 0) {
            if (log.isDebugEnabled()) {
                log.debug("No changes to send to the server");
            }
            return;
        }
        ITunesRemoteClient client = new ITunesRemoteClient();
        client.connect(hostname, port);
        client.login(user, password);
        try {
            updateITunes(client);
        } catch (StoreException e) {
            log.error(Messages.getString("RemoteMacOSXItunesStore.UNABLE_UPDATE"), e);
        }
        client.disconnect();
    }
