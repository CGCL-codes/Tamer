    @Override
    public void requestConnection() {
        if (m_strId == null || isConnected()) return;
        initChannelAccess();
        if (_jcaChannel == null) {
            try {
                synchronized (_connectionLock) {
                    _jcaChannel = _jcaNativeChannelCache.getChannel(m_strId);
                    _jcaChannel.addConnectionListener(newConnectionListener());
                    if (_jcaChannel.getConnectionState() == gov.aps.jca.Channel.CONNECTED) {
                        processConnectionEvent();
                    }
                }
            } catch (CAException exception) {
                final String message = "Error attempting to connect to: " + m_strId;
                Logger.getLogger("global").log(Level.SEVERE, message, exception);
            }
        }
    }
