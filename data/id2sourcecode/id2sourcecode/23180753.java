    void selectorThreadBody() {
        fRunning = true;
        while (!this.isStopRequested()) {
            synchronized (fChangeRequests) {
                for (final ChangeRequest change : fChangeRequests) {
                    try {
                        switch(change.getType()) {
                            case CHANGE_OPS:
                                {
                                    final ConnectionInfo connInfo = change.getConnectionInfo();
                                    final SelectionKey key = connInfo.getSelectionKey();
                                    if (key != null) {
                                        key.interestOps(change.getOps());
                                    } else {
                                        logger.warn(AbstractInSimNioClient.FORMAT_UTILS.format(LogMessages.getString("Client.channel.nullKey"), change));
                                    }
                                    break;
                                }
                            case REGISTER:
                                {
                                    final ConnectionInfo connInfo = change.getConnectionInfo();
                                    final SelectableChannel channel = connInfo.getChannel();
                                    final SelectionKey key = channel.register(fSelector, change.getOps());
                                    key.attach(connInfo);
                                    connInfo.setSelectionKey(key);
                                    break;
                                }
                            case CANCEL:
                                {
                                    final ConnectionInfo connInfo = change.getConnectionInfo();
                                    final SelectionKey key = connInfo.getSelectionKey();
                                    key.cancel();
                                    key.channel().close();
                                    break;
                                }
                        }
                    } catch (final ClosedChannelException cche) {
                        logger.warn("Exception: ", cche);
                    } catch (final IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                }
                fChangeRequests.clear();
            }
            try {
                fSelector.select(AbstractInSimNioClient.SELECTOR_TIMEOUT);
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
            final Iterator<SelectionKey> selectedKeys = fSelector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                final SelectionKey key = selectedKeys.next();
                selectedKeys.remove();
                if (!key.isValid()) {
                    continue;
                }
                try {
                    if (key.isConnectable()) {
                        this.finishConnection(key);
                    } else if (key.isReadable()) {
                        this.read(key);
                        final ConnectionInfo connInfo = (ConnectionInfo) key.attachment();
                        final int size = fReadBuffer.position();
                        if (size == 0) {
                            logger.warn("Empty packet recieved from connection " + connInfo.getHost());
                            continue;
                        }
                        final byte[] data = new byte[size];
                        System.arraycopy(fReadBuffer.array(), 0, data, 0, data.length);
                        this.handleResponse(connInfo, data);
                    } else if (key.isWritable()) {
                        this.write(key);
                    }
                } catch (final IOException ioe) {
                    final ConnectionInfo connInfo = (ConnectionInfo) key.attachment();
                    logger.debug(AbstractInSimNioClient.FORMAT_UTILS.format(ExceptionMessages.getString("Client.ioe.channel"), key.channel(), ioe));
                    this.handleException(connInfo, ioe);
                }
            }
        }
        fRunning = false;
        this.notifyStateListeners(InSimClientState.STOPPED);
    }
