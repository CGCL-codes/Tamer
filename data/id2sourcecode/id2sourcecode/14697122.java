    @Override
    public void run() {
        synchronized (this) {
            if (status != Status.STARTED) {
                log.warn("Started the thread, but the current status is: " + status.toString() + ",\n" + "terminating thread immediately");
                return;
            }
            try {
                servSckChannel = ServerSocketChannel.open();
                servSckChannel.configureBlocking(false);
                servSckChannel.socket().bind(new InetSocketAddress(targetPort));
                port = targetPort;
                log.info("Listening on port [ " + targetPort + " ]");
            } catch (BindException e) {
                status = Status.OFFLINE;
                log.error("Attempted to bind to port " + targetPort + " which is already in use; server going offline", e);
                return;
            } catch (IOException e) {
                status = Status.OFFLINE;
                log.error("Failed to open non-blocking server port = " + targetPort + "; server going offline", e);
                return;
            }
        }
        try {
            synchronized (this) {
                if (status != Status.STARTED) return;
                selector = Selector.open();
                servSckChannel.register(selector, SelectionKey.OP_ACCEPT);
                status = Status.RUNNING;
            }
            while (thisThread != null) {
                synchronized (connectableChannels) {
                    Iterator<SocketChannel> changes = connectableChannels.iterator();
                    while (changes.hasNext()) {
                        SocketChannel change = changes.next();
                        change.register(selector, SelectionKey.OP_CONNECT);
                    }
                    this.connectableChannels.clear();
                }
                log.debug("[" + ((thisThread == null) ? "null" : thisThread.getName()) + ":" + port + "] Selecting ");
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = null;
                    log.debug("Next key");
                    try {
                        key = iterator.next();
                        iterator.remove();
                        if (key.isConnectable()) {
                            log.debug("Connectable key, completing the connection");
                            SocketChannel sc = ((SocketChannel) key.channel());
                            sc.finishConnect();
                            key.interestOps(SelectionKey.OP_READ);
                            synchronized (sc) {
                                sc.notifyAll();
                            }
                        }
                        if (key.isAcceptable()) {
                            log.debug("Acceptable key, accepting and adding new SocketChannel to selector for reading");
                            SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            connectionAccepted(sc);
                        }
                        if (key.isReadable()) {
                            log.debug("Readable key");
                            try {
                                Object readObject = getObjectFromKey(key);
                                log.info("[" + ((thisThread == null) ? "null" : thisThread.getName()) + ":" + port + "]   Received object = \"" + readObject);
                                onObjectRead(readObject, key);
                            } catch (IOException e) {
                                log.info("\n\t\tClient probably disconected I'll close the socket");
                                log.debug("Cancelling readable key (" + key + ")");
                                onChannelClose(key.channel());
                                key.channel().close();
                                key.cancel();
                                continue;
                            }
                        }
                        if (key.isWritable()) {
                            log.debug("Writable key");
                            synchronized (mapQueues) {
                                Queue<ObjToSend> queue = mapQueues.get(key);
                                while (!queue.isEmpty()) {
                                    ObjToSend objts = queue.remove();
                                    byte[] bytes = serializeObject((Serializable) objts.obj);
                                    ByteBuffer bbf = createNetworkObject(bytes);
                                    int numWritten = ((WritableByteChannel) objts.sc).write(bbf);
                                }
                                if (queue.isEmpty()) {
                                    key.interestOps(SelectionKey.OP_READ);
                                }
                            }
                        }
                    } catch (CancelledKeyException e) {
                        log.warn("Attempted to write to cancelled key (probably " + "cancelled on read in this loop) (" + key + "); ignoring ", e);
                        continue;
                    } catch (IOException e) {
                        log.info("Cancelling writable key (" + key + ") that generated: ", e);
                        key.cancel();
                        continue;
                    } catch (Exception e) {
                        log.warn("[" + ((thisThread == null) ? "null" : thisThread.getName()) + ":" + port + "] Error handling key = " + key + ", continuing with selection for all other keys", e);
                    }
                    log.debug("Key ended; keys left this run = " + iterator.hasNext());
                }
                log.debug("Last key ended");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "The server went to hell due to" + e + ", please restart NK");
            log.error("The server went to hell ", e);
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(null, "The server went to hell due to" + e + ", please restart NK");
            log.error("The server went to hell ", e);
        } finally {
            try {
                selector.close();
            } catch (Throwable e) {
                log.error("Failed on final attempt to close selector", e);
            }
            try {
                servSckChannel.close();
            } catch (Throwable e) {
                log.error("Failed on final attempt to close ServerSocketChannel", e);
            }
        }
    }
