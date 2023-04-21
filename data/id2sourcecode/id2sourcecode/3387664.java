        public void updateLimits() {
            synchronized (KnobElement.this) {
                if (this != _limitsHandler) return;
                if (_lowerChannel != null && _lowerChannel.isConnected()) {
                    try {
                        _lowerChannel.getValDblCallback(this);
                    } catch (ConnectionException exception) {
                    } catch (GetException exception) {
                    }
                }
                if (_upperChannel != null && _upperChannel.isConnected()) {
                    try {
                        _upperChannel.getValDblCallback(this);
                    } catch (ConnectionException exception) {
                    } catch (GetException exception) {
                    }
                }
                Channel.flushIO();
            }
            EVENT_PROXY.channelChanged(KnobElement.this, KnobElement.this.getChannel());
            EVENT_PROXY.readyStateChanged(KnobElement.this, KnobElement.this.isReady());
        }
