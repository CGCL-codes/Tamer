    public void doNetIo() {
        Iterator listenersIt = connectionListeners.iterator();
        while (listenersIt.hasNext()) {
            ConnectionListener listener = (ConnectionListener) listenersIt.next();
            while (numConnections() < getMaxConcurrentConnections() && listener.hasNewConnection()) {
                Connection c = listener.getNextNewConnection();
                SocketChannel sc = c.getChannel();
                try {
                    c.setSelectionKey(sc.register(readSelector, SelectionKey.OP_READ, c));
                    channelWrappers.add(c);
                    c.setConnected(true);
                    log.fine("Added new connection... " + c.getConnectionNumber());
                } catch (ClosedChannelException e) {
                    log.fine(c.toString() + "|" + e.getMessage());
                }
            }
            if (numConnections() >= getMaxConcurrentConnections()) {
                if (!maxConnectionsReached) {
                    log.info("maximum allowed number of connections reached ( (" + channelWrappers.size() + "+" + pendingConnections.size() + ")/" + getMaxConcurrentConnections() + ")");
                }
                maxConnectionsReached = true;
            } else maxConnectionsReached = false;
        }
        synchronized (pendingConnections) {
            while (!pendingConnections.isEmpty()) {
                Connection c = (Connection) pendingConnections.removeFirst();
                newConnection(c);
            }
        }
        synchronized (pendingConnectionListeners) {
            while (!pendingConnectionListeners.isEmpty()) {
                ConnectionListener cl = null;
                try {
                    cl = (ConnectionListener) pendingConnectionListeners.removeFirst();
                    cl.init();
                    connectionListeners.add(cl);
                } catch (Exception e) {
                    log.warning(e.getClass().getName() + ":" + cl.toString() + "|" + e.getMessage());
                }
            }
        }
        int newKeys;
        try {
            long now = System.currentTimeMillis();
            long selecttime = 100L - now + lastNetIoTime;
            if (selecttime <= 0) {
                selecttime = 1;
            }
            lastNetIoTime = now;
            newKeys = readSelector.select(selecttime);
            testselectmode();
            if (newKeys > 0) {
                Set keys = readSelector.selectedKeys();
                Iterator i = keys.iterator();
                while (i.hasNext()) {
                    SelectionKey nextKey = (SelectionKey) i.next();
                    i.remove();
                    if (nextKey.isConnectable()) {
                        Connection c = (Connection) nextKey.attachment();
                        SocketChannel sc = (SocketChannel) nextKey.channel();
                        try {
                            log.finest("finishConnect start");
                            boolean result = sc.finishConnect();
                            assert result == true;
                            log.finest("finishConnect end");
                            nextKey.interestOps(SelectionKey.OP_READ);
                            c.setSelectionKey(nextKey);
                            channelWrappers.add(c);
                            c.setConnected(true);
                        } catch (ConnectException e) {
                            if (e.getMessage().startsWith("Connection refused")) {
                                c.setConnected(false);
                            }
                            if (e.getMessage().startsWith("Connection timed out")) ;
                            log.log(Level.FINE, "connect failed to " + c.getPeerAddress(), e);
                            c.close();
                        } catch (NoRouteToHostException e) {
                            log.log(Level.FINE, "connect failed (no route to host) to " + c.getPeerAddress(), e);
                            c.close();
                        } catch (java.net.BindException e) {
                            if (e.getMessage().startsWith("Cannot assign requested address: ")) {
                                c.setConnected(false);
                                log.fine("connection to " + c.getPeerAddress() + " faild:  " + e.toString());
                            } else {
                                log.config(e.getMessage());
                                log.warning(c.getConnectionNumber() + " Error making connection to " + c.getPeerAddress() + ": " + e.toString());
                            }
                        } catch (SocketException e) {
                            log.log(Level.WARNING, "connect failed to " + c.getPeerAddress(), e);
                            c.close();
                        }
                    } else {
                        if (nextKey.isReadable()) {
                            ChannelWrapper c = (ChannelWrapper) nextKey.attachment();
                            if (c instanceof DonkeyConnection) {
                                DonkeyDownLoadLimiter.Limiter.incConnections();
                                reading.add(c);
                            } else {
                                try {
                                    if (!c.processInput()) {
                                        log.fine(c.hashCode() + " Channel closed.");
                                        c.close();
                                        channelWrappers.remove(c);
                                    }
                                } catch (IOException ioe) {
                                    c.close();
                                    channelWrappers.remove(c);
                                }
                            }
                        } else if (!nextKey.isWritable()) {
                            ChannelWrapper c = (ChannelWrapper) nextKey.attachment();
                            log.warning("connection going into limbo: " + c.getClass().getName() + ((c instanceof Connection) ? " " + ((Connection) c).getConnectionNumber() : ""));
                        } else {
                            ChannelWrapper c = (ChannelWrapper) nextKey.attachment();
                            if (c instanceof DonkeyConnection) {
                                DonkeyUpLoadLimiter.Limiter.incConnections();
                                writeing.add(c);
                            } else {
                                c.processOutput();
                            }
                        }
                    }
                }
                while (!writeing.isEmpty()) {
                    ChannelWrapper c = (ChannelWrapper) writeing.remove(0);
                    c.processOutput();
                }
                while (!reading.isEmpty()) {
                    ChannelWrapper c = (ChannelWrapper) reading.remove(0);
                    try {
                        if (!c.processInput()) {
                            log.fine(c.hashCode() + " Channel closed.");
                            c.close();
                            channelWrappers.remove(c);
                        }
                    } catch (ClosedChannelException e) {
                        log.fine(c.hashCode() + " Channel closed.");
                        c.close();
                        channelWrappers.remove(c);
                    } catch (IOException e) {
                        log.fine(c.hashCode() + " Error reading bytes: " + e.getMessage());
                        c.close();
                        channelWrappers.remove(c);
                    }
                }
            }
        } catch (IOException e) {
            log.warning("ConnectionManager read/connect error: " + e.getClass() + " : " + e.getMessage());
        }
    }
