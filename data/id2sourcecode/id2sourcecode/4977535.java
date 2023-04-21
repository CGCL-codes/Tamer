        protected void handleConnected(ConnectedEvent connEvent) {
            InetAddress dest = null;
            ConnEntry ent = null;
            try {
                SocketChannel channel = connEvent.getChannel();
                synchronized (resolveMap) {
                    dest = resolveMap.get(channel);
                }
                synchronized (connPool) {
                    ent = connPool.get(dest);
                }
                logging.debug(LOG_NAME, "NIO client: conn established dest=" + dest);
                synchronized (ent) {
                    ent.established(channel);
                    for (Iterator<SendRequestEvent> i = ent.getPendings().iterator(); i.hasNext(); ) {
                        SendRequestEvent se = i.next();
                        sendRequest(ent.getOutputStream(), se, channel);
                        i.remove();
                    }
                }
                ConnEvent ce = new ConnEvent(dest);
                for (BlockingQueue<Event> q : failQueues) q.put(ce);
            } catch (IOException e) {
                if (keepSilent) logging.warning(LOG_NAME, "conn failed dest=" + dest); else e.printStackTrace();
                try {
                    for (SendRequestEvent se : ent.getPendings()) {
                        se.getQueue().put(new SendFailedEvent(se.getDest(), se.getTag(), se.getMessage(), false));
                    }
                } catch (InterruptedException e2) {
                }
                discardConnection(connEvent.getChannel());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
