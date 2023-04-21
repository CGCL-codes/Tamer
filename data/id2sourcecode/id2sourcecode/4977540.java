        protected void handleConnFailed(ConnectFailedEvent fe) {
            InetAddress dest = null;
            synchronized (resolveMap) {
                dest = resolveMap.get(fe.getChannel());
            }
            if (dest != null) {
                logging.warning(LOG_NAME, "connection failed to dest=" + dest);
                ConnEntry ent = null;
                synchronized (connPool) {
                    ent = connPool.get(dest);
                }
                if (ent != null) {
                    try {
                        for (SendRequestEvent se : ent.getPendings()) {
                            se.getQueue().put(new SendFailedEvent(se.getDest(), se.getTag(), se.getMessage(), false));
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
            discardConnection(fe.getChannel());
        }
