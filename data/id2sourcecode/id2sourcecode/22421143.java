    protected ChannelWrapper wrapChannel(final String handle) {
        Channel channel = _powerSupply.getChannel(handle);
        ChannelWrapper wrapper = new ChannelWrapper(channel);
        wrapper.addChannelEventListener(new ChannelEventListener() {

            /**
				 * The PV's monitored value has changed.
				 * @param channel  the channel whose value has changed
				 * @param record   The channel time record of the new value
				 */
            public void valueChanged(Channel channel, ChannelTimeRecord record) {
                _latestField = record.doubleValue();
                EVENT_PROXY.fieldChanged(CorrectorSupply.this, record, _latestField);
            }

            /**
				 * The channel's connection has changed. Either it has established a new connection or the existing connection has dropped.
				 * @param channel    The channel whose connection has changed.
				 * @param connected  The channel's new connection state
				 */
            public void connectionChanged(Channel channel, boolean connected) {
                _latestField = Double.NaN;
                EVENT_PROXY.connectionChanged(CorrectorSupply.this, connected);
            }
        });
        wrapper.requestConnection();
        return wrapper;
    }
