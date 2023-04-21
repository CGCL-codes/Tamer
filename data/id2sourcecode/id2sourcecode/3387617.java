    public void addKnobElementListener(final KnobElementListener listener) {
        synchronized (this) {
            MESSAGE_CENTER.registerTarget(listener, this, KnobElementListener.class);
            listener.channelChanged(this, getChannel());
            listener.connectionChanged(this, isConnected());
            listener.readyStateChanged(this, isReady());
            listener.coefficientChanged(this, getCoefficient());
            listener.valueChanged(this, getLatestValue());
        }
    }
