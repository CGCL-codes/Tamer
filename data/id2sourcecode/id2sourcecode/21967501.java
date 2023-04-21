        public void onManagerEvent(ManagerEvent event) {
            if (event instanceof HangupEvent) {
                String uid = ((HangupEvent) event).getUniqueId();
                synchronized (channelKeepaliveMap) {
                    KeepAlive ka = channelKeepaliveMap.remove(uid);
                    if (ka != null) {
                        if (SafletEngine.debuggerLog.isDebugEnabled()) SafletEngine.debuggerLog.debug("Loopback channel hangup event detected for chan " + ((HangupEvent) event).getChannel());
                        ka.stop();
                    }
                }
            }
        }
