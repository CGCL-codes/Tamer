        public void setChannel(final Channel channel) {
            synchronized (this) {
                if (_lowerChannel != null) {
                    _lowerChannel.removeConnectionListener(this);
                }
                final String lowerLimitPV = _channel.channelName() + ".LOPR";
                _lowerChannel = ChannelFactory.defaultFactory().getChannel(lowerLimitPV);
                _lowerChannel.addConnectionListener(this);
                _lowerChannel.requestConnection();
                if (_upperChannel != null) {
                    _upperChannel.removeConnectionListener(this);
                }
                final String upperLimitPV = _channel.channelName() + ".HOPR";
                _upperChannel = ChannelFactory.defaultFactory().getChannel(upperLimitPV);
                _upperChannel.addConnectionListener(this);
                _upperChannel.requestConnection();
            }
        }
