    public AsyncEngine(boolean daemon) throws IOException {
        selector = Selector.open();
        Thread main = new Thread(new Runnable() {

            public void run() {
                while (selector.isOpen()) {
                    try {
                        LOG.finest("calling select ()");
                        selector.select();
                    } catch (CancelledKeyException e) {
                        LOG.log(Level.FINEST, "Cancelled key exception", e);
                    } catch (IOException e) {
                        if (!e.getMessage().equals("Interrupted system call")) {
                            LOG.log(Level.SEVERE, "unhandled exception: " + e, e);
                            System.exit(1);
                        }
                    }
                    LOG.finest("select returned");
                    if (selector.isOpen()) {
                        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                        while (keyIterator.hasNext()) {
                            SelectionKey skey = keyIterator.next();
                            keyIterator.remove();
                            SelectableInfo si = selectableChannels.get(skey.channel());
                            if (si == null) continue;
                            if (skey.isValid() && (si.accept != null) && ((skey.readyOps() & OP_ACCEPT) != 0)) {
                                si.accept.run();
                            }
                            if (skey.isValid() && (si.connect != null) && ((skey.readyOps() & OP_CONNECT) != 0)) {
                                si.connect.run();
                            }
                            if (skey.isValid() && (si.read != null) && ((skey.readyOps() & OP_READ) != 0)) {
                                si.read.run();
                            }
                            if (skey.isValid() && (si.write != null) && ((skey.readyOps() & OP_WRITE) != 0)) {
                                si.write.run();
                            }
                        }
                    }
                    for (Runnable runnable = changes.poll(); runnable != null; runnable = changes.poll()) {
                        runnable.run();
                    }
                }
            }
        }, "async-engine");
        main.setDaemon(daemon);
        main.start();
    }
