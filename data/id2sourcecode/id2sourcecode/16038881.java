        private void handleEvent(Event r) {
            ExchangeImpl t = r.exchange;
            HttpConnection c = t.getConnection();
            try {
                if (r instanceof WriteFinishedEvent) {
                    int exchanges = endExchange();
                    if (terminating && exchanges == 0) {
                        finished = true;
                    }
                    SocketChannel chan = c.getChannel();
                    LeftOverInputStream is = t.getOriginalInputStream();
                    if (!is.isEOF()) {
                        t.close = true;
                    }
                    if (t.close || idleConnections.size() >= MAX_IDLE_CONNECTIONS) {
                        c.close();
                        allConnections.remove(c);
                    } else {
                        if (is.isDataBuffered()) {
                            handle(c.getChannel(), c);
                        } else {
                            SelectionKey key = c.getSelectionKey();
                            if (key.isValid()) {
                                key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                            }
                            c.time = getTime() + IDLE_INTERVAL;
                            idleConnections.add(c);
                        }
                    }
                }
            } catch (IOException e) {
                logger.log(Level.FINER, "Dispatcher (1)", e);
                c.close();
            }
        }
