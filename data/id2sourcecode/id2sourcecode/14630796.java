        void onConnected(int _negotiatedSessionTimeout, long _sessionId, byte[] _sessionPasswd, boolean isRO) throws IOException {
            negotiatedSessionTimeout = _negotiatedSessionTimeout;
            if (negotiatedSessionTimeout <= 0) {
                state = States.CLOSED;
                eventThread.queueEvent(new WatchedEvent(Watcher.Event.EventType.None, Watcher.Event.KeeperState.Expired, null));
                eventThread.queueEventOfDeath();
                throw new SessionExpiredException("Unable to reconnect to ZooKeeper service, session 0x" + Long.toHexString(sessionId) + " has expired");
            }
            if (!readOnly && isRO) {
                LOG.error("Read/write client got connected to read-only server");
            }
            readTimeout = negotiatedSessionTimeout * 2 / 3;
            connectTimeout = negotiatedSessionTimeout / hostProvider.size();
            hostProvider.onConnected();
            sessionId = _sessionId;
            sessionPasswd = _sessionPasswd;
            state = (isRO) ? States.CONNECTEDREADONLY : States.CONNECTED;
            seenRwServerBefore |= !isRO;
            LOG.info("Session establishment complete on server " + clientCnxnSocket.getRemoteSocketAddress() + ", sessionid = 0x" + Long.toHexString(sessionId) + ", negotiated timeout = " + negotiatedSessionTimeout + (isRO ? " (READ-ONLY mode)" : ""));
            KeeperState eventState = (isRO) ? KeeperState.ConnectedReadOnly : KeeperState.SyncConnected;
            eventThread.queueEvent(new WatchedEvent(Watcher.Event.EventType.None, eventState, null));
        }
