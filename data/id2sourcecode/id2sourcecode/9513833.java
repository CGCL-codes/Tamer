    private void processChangeRequests() {
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
                                    logger.warn(fFormatUtils.format(LogMessages.getString("Client.channel.nullKey"), change));
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
    }
