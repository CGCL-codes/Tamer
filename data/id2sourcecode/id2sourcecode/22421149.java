        public void setChannel(final Channel channel) {
            synchronized (this) {
                if (_lowerChannel != null) {
                    _lowerChannel.removeConnectionListener(this);
                    if (_lowerMonitor != null) {
                        _lowerMonitor.clear();
                        _lowerMonitor = null;
                    }
                }
                final String lowerLimitPV = channel.channelName() + ".LOPR";
                _lowerChannel = ChannelFactory.defaultFactory().getChannel(lowerLimitPV);
                _lowerChannel.addConnectionListener(this);
                _lowerChannel.requestConnection();
                if (_upperChannel != null) {
                    _upperChannel.removeConnectionListener(this);
                    if (_upperMonitor != null) {
                        _upperMonitor.clear();
                        _upperMonitor = null;
                    }
                }
                final String upperLimitPV = channel.channelName() + ".HOPR";
                _upperChannel = ChannelFactory.defaultFactory().getChannel(upperLimitPV);
                _upperChannel.addConnectionListener(this);
                _upperChannel.requestConnection();
            }
        }
