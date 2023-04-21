        protected void handleRequest(SendRequestEvent se) {
            ConnEntry ent = null;
            boolean needEstablish = false;
            synchronized (connPool) {
                ent = connPool.get(se.getDest());
                if (ent == null) {
                    ent = connPool.put(se.getDest(), new ConnEntry(se));
                    needEstablish = true;
                }
            }
            if (needEstablish) {
                prepareConnection(se);
                return;
            }
            synchronized (ent) {
                if (ent.isConnected()) {
                    try {
                        sendRequest(ent.getOutputStream(), se, ent.getChannel());
                    } catch (IOException e) {
                        if (keepSilent) logging.warning(LOG_NAME, "send failed dest=" + se.getDest()); else e.printStackTrace();
                        try {
                            se.getQueue().put(new SendFailedEvent(se.getDest(), se.getTag(), se.getMessage(), false));
                        } catch (InterruptedException e2) {
                        }
                        discardConnection(ent.getChannel());
                    }
                } else {
                    ent.add(se);
                }
            }
        }
