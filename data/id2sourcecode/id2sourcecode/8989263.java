    private IRemoteVault getRemoteBroadcaster() {
        return (IRemoteVault) m_channelMessenger.getChannelBroadcastor(VAULT_CHANNEL);
    }
