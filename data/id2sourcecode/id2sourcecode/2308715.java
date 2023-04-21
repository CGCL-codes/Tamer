        public void run() {
            try {
                while (!stop) {
                    try {
                        if (selector.select() > 0) {
                            if (stop) {
                                break;
                            }
                            Set readyKeys = selector.selectedKeys();
                            Iterator it = readyKeys.iterator();
                            while (it.hasNext()) {
                                try {
                                    SelectionKey sk = (SelectionKey) it.next();
                                    it.remove();
                                    SocketChannel readChannel = null;
                                    TcpAddress incomingAddress = null;
                                    if (sk.isAcceptable()) {
                                        ServerSocketChannel nextReady = (ServerSocketChannel) sk.channel();
                                        Socket s = nextReady.accept().socket();
                                        readChannel = s.getChannel();
                                        readChannel.configureBlocking(false);
                                        readChannel.register(selector, SelectionKey.OP_READ);
                                        incomingAddress = new TcpAddress(s.getInetAddress(), s.getPort());
                                        SocketEntry entry = new SocketEntry(incomingAddress, s);
                                        sockets.put(incomingAddress, entry);
                                        timeoutSocket(entry);
                                        TransportStateEvent e = new TransportStateEvent(DefaultTcpTransportMapping.this, incomingAddress, TransportStateEvent.STATE_CONNECTED, null);
                                        fireConnectionStateChanged(e);
                                        if (e.isCancelled()) {
                                            logger.warn("Incoming connection cancelled");
                                            s.close();
                                            sockets.remove(incomingAddress);
                                            readChannel = null;
                                        }
                                    } else if (sk.isReadable()) {
                                        readChannel = (SocketChannel) sk.channel();
                                        incomingAddress = new TcpAddress(readChannel.socket().getInetAddress(), readChannel.socket().getPort());
                                    } else if (sk.isWritable()) {
                                        try {
                                            SocketChannel sc = (SocketChannel) sk.channel();
                                            SocketEntry entry;
                                            synchronized (pending) {
                                                try {
                                                    entry = (SocketEntry) pending.removeFirst();
                                                } catch (NoSuchElementException nsex) {
                                                    entry = null;
                                                }
                                            }
                                            if (entry != null) {
                                                writeMessage(entry, sc);
                                            }
                                        } catch (IOException iox) {
                                            if (logger.isDebugEnabled()) {
                                                iox.printStackTrace();
                                            }
                                            logger.warn(iox);
                                            TransportStateEvent e = new TransportStateEvent(DefaultTcpTransportMapping.this, incomingAddress, TransportStateEvent.STATE_DISCONNECTED_REMOTELY, iox);
                                            fireConnectionStateChanged(e);
                                            sk.cancel();
                                        }
                                    } else if (sk.isConnectable()) {
                                        try {
                                            SocketEntry entry;
                                            synchronized (pending) {
                                                try {
                                                    entry = (SocketEntry) pending.getFirst();
                                                    if (entry != null) {
                                                        SocketChannel sc = (SocketChannel) sk.channel();
                                                        if ((!sc.isConnected()) && (sc.finishConnect())) {
                                                            sc.configureBlocking(false);
                                                            logger.debug("Connected to " + entry.getPeerAddress());
                                                            timeoutSocket(entry);
                                                            sc.register(selector, SelectionKey.OP_WRITE);
                                                        }
                                                    }
                                                } catch (NoSuchElementException nsex) {
                                                    entry = null;
                                                }
                                            }
                                            if (entry != null) {
                                                TransportStateEvent e = new TransportStateEvent(DefaultTcpTransportMapping.this, incomingAddress, TransportStateEvent.STATE_CONNECTED, null);
                                                fireConnectionStateChanged(e);
                                            } else {
                                                logger.warn("Message not found on finish connection");
                                            }
                                        } catch (IOException iox) {
                                            if (logger.isDebugEnabled()) {
                                                iox.printStackTrace();
                                            }
                                            logger.warn(iox);
                                            sk.cancel();
                                        }
                                    }
                                    if (readChannel != null) {
                                        try {
                                            readMessage(sk, readChannel, incomingAddress);
                                        } catch (IOException iox) {
                                            if (logger.isDebugEnabled()) {
                                                iox.printStackTrace();
                                            }
                                            logger.warn(iox);
                                            sk.cancel();
                                            readChannel.close();
                                            TransportStateEvent e = new TransportStateEvent(DefaultTcpTransportMapping.this, incomingAddress, TransportStateEvent.STATE_DISCONNECTED_REMOTELY, iox);
                                            fireConnectionStateChanged(e);
                                        }
                                    }
                                } catch (CancelledKeyException ckex) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("Selection key cancelled, skipping it");
                                    }
                                }
                            }
                        }
                    } catch (NullPointerException npex) {
                        npex.printStackTrace();
                        logger.warn("NullPointerException within select()?");
                        stop = true;
                    }
                    processPending();
                }
                if (ssc != null) {
                    ssc.close();
                }
                if (selector != null) {
                    selector.close();
                }
            } catch (IOException iox) {
                logger.error(iox);
                lastError = iox;
            }
            if (!stop) {
                stop = true;
                synchronized (DefaultTcpTransportMapping.this) {
                    server = null;
                }
            }
        }
