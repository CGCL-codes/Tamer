    public void onManagerEvent(ManagerEvent event) {
        _logger.debug(event.toString());
        _logger.info("Asterisk Event: " + event.getClass().getCanonicalName());
        if (event instanceof org.asteriskjava.manager.event.HangupEvent) {
            HangupEvent e = (HangupEvent) event;
            String channel = e.getChannel().trim();
            _logger.info("got a hangup on the channel: " + e.getChannel() + "|" + _channel);
            if (channel.equals(_channel)) {
                synchronized (this) {
                    _callCompleted = true;
                    this.notifyAll();
                }
            }
        }
    }
