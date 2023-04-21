    private void processUpdate() {
        for (DaapConnectionNIO connection : getDaapConnections()) {
            SelectionKey sk = connection.getChannel().keyFor(selector);
            try {
                for (int i = 0; i < libraryQueue.size(); i++) {
                    connection.enqueueLibrary(libraryQueue.get(i));
                }
                connection.update();
                if (sk.isValid()) {
                    try {
                        sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    } catch (CancelledKeyException err) {
                        cancel(sk);
                        LOG.error("SelectionKey.interestOps()", err);
                    }
                }
            } catch (ClosedChannelException err) {
                cancel(sk);
                LOG.error("DaapConnection.update()", err);
            } catch (IOException err) {
                cancel(sk);
                LOG.error("DaapConnection.update()", err);
            }
        }
        libraryQueue.clear();
    }
