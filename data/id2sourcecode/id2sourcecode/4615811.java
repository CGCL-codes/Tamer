    public void run() {
        while (!isStop()) {
            try {
                synchronized (fChangeRequests) {
                    final Iterator<ChangeRequest> changes = fChangeRequests.iterator();
                    while (changes.hasNext()) {
                        final ChangeRequest change = changes.next();
                        switch(change.getType()) {
                            case ChangeRequest.CHANGEOPS:
                                final SelectionKey key = change.getChannel().keyFor(fSelector);
                                if (key != null) {
                                    key.interestOps(change.getOps());
                                } else {
                                    log.debug(FORMAT_UTILS.format(LogMessages.getString("Client.channel.nullKey"), change));
                                }
                                break;
                            case ChangeRequest.REGISTER:
                                change.getChannel().register(fSelector, change.getOps());
                                break;
                            case ChangeRequest.CANCEL:
                                change.getChannel().keyFor(fSelector).cancel();
                                change.getChannel().close();
                                break;
                        }
                    }
                    fChangeRequests.clear();
                }
                fSelector.select();
                final Iterator<SelectionKey> selectedKeys = fSelector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    final SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isConnectable()) {
                        this.finishConnection(key);
                    } else if (key.isReadable()) {
                        this.read(key);
                    } else if (key.isWritable()) {
                        this.write(key);
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
