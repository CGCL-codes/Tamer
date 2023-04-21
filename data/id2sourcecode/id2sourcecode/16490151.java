    @Override
    public void run() {
        SocketChannel channel = null;
        DHTBroadcaster<K> s = null;
        mythread = Thread.currentThread();
        Entry<K, DHTTransport> e;
        K n;
        DHTTransport tc;
        while (goon) {
            try {
                channel = cf.getChannel();
                if (logger.isDebugEnabled()) {
                    logger.debug("dls channel = " + channel);
                }
                if (mythread.isInterrupted()) {
                    goon = false;
                } else {
                    s = new DHTBroadcaster<K>(channel, servers, theList);
                    int ls = 0;
                    synchronized (servers) {
                        if (goon) {
                            servers.add(s);
                            ls = theList.size();
                            s.start();
                        }
                    }
                    if (logger.isInfoEnabled()) {
                        logger.info("server " + s + " started " + s.isAlive());
                    }
                    if (ls > 0) {
                        synchronized (theList) {
                            Iterator<Entry<K, DHTTransport>> it = theList.entrySet().iterator();
                            for (int i = 0; i < ls; i++) {
                                e = it.next();
                                n = e.getKey();
                                tc = e.getValue();
                                try {
                                    s.sendChannel(tc);
                                } catch (IOException ioe) {
                                }
                            }
                        }
                    }
                }
            } catch (InterruptedException end) {
                goon = false;
                Thread.currentThread().interrupt();
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("listserver " + this + " terminated");
        }
    }
