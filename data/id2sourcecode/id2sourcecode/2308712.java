        private void processPending() {
            synchronized (pending) {
                for (int i = 0; i < pending.size(); i++) {
                    SocketEntry entry = (SocketEntry) pending.getFirst();
                    try {
                        if (entry.getSocket().isConnected()) {
                            entry.getSocket().getChannel().register(selector, SelectionKey.OP_WRITE);
                        } else {
                            entry.getSocket().getChannel().register(selector, SelectionKey.OP_CONNECT);
                        }
                    } catch (IOException iox) {
                        logger.error(iox);
                        try {
                            entry.getSocket().getChannel().close();
                            TransportStateEvent e = new TransportStateEvent(DefaultTcpTransportMapping.this, entry.getPeerAddress(), TransportStateEvent.STATE_CLOSED, iox);
                            fireConnectionStateChanged(e);
                        } catch (IOException ex) {
                            logger.error(ex);
                        }
                        lastError = iox;
                        if (SNMP4JSettings.isFowardRuntimeExceptions()) {
                            throw new RuntimeException(iox);
                        }
                    }
                }
            }
        }
