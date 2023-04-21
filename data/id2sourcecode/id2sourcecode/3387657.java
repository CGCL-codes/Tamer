        public void updateLimits() {
            if (this == _limitsHandler) {
                _lowerLimit = _lowerCustomLimit;
                _upperLimit = _upperCustomLimit;
                EVENT_PROXY.channelChanged(KnobElement.this, KnobElement.this.getChannel());
                EVENT_PROXY.readyStateChanged(KnobElement.this, KnobElement.this.isReady());
            }
        }
