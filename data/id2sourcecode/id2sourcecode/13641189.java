        protected void registerCanceledKeys() {
            synchronized (canceled) {
                for (Iterator<CanceledEntry> i = canceled.iterator(); i.hasNext(); ) {
                    CanceledEntry e = i.next();
                    e.setCleaned();
                    if (e.canRegister()) {
                        SelectableChannel channel = e.getChannel();
                        try {
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                        } catch (IOException ex) {
                            try {
                                if (channel instanceof SocketChannel) {
                                    ConnFailedEvent fe = new ConnFailedEvent(((SocketChannel) channel).socket().getInetAddress());
                                    synchronized (failQueues) {
                                        for (BlockingQueue<Event> q : failQueues) q.put(fe);
                                    }
                                }
                                channel.close();
                            } catch (IOException ex2) {
                            } catch (InterruptedException ex2) {
                            }
                        }
                        i.remove();
                    }
                }
            }
        }
