    protected Channel monitorChannel(final String handle, final RecordFilter filter) {
        Channel channel = _bpm.getChannel(handle);
        _channelTable.put(handle, channel);
        correlateSignal(channel, filter);
        channel.addConnectionListener(new ConnectionListener() {

            /**
				 * Indicates that a connection to the specified channel has been established.
				 * @param channel  The channel which has been connected.
				 */
            public void connectionMade(Channel channel) {
                synchronized (_eventLock) {
                    _lastRecord = null;
                    correlateSignal(channel, filter);
                    EVENT_PROXY.connectionChanged(BpmAgent.this, handle, true);
                }
            }

            /**
				 * Indicates that a connection to the specified channel has been dropped.
				 * @param channel  The channel which has been disconnected.
				 */
            public void connectionDropped(Channel channel) {
                synchronized (_eventLock) {
                    _lastRecord = null;
                    EVENT_PROXY.connectionChanged(BpmAgent.this, handle, false);
                }
            }
        });
        if (!channel.isConnected()) {
            channel.requestConnection();
        }
        return channel;
    }
