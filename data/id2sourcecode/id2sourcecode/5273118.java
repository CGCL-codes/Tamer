    @Override
    public void run() {
        SocketChannel channel = null;
        Broadcaster s = null;
        mythread = Thread.currentThread();
        Entry e;
        Object n;
        Object o;
        while (goon) {
            try {
                channel = cf.getChannel();
                logger.debug("dls channel = " + channel);
                if (mythread.isInterrupted()) {
                    goon = false;
                } else {
                    s = new Broadcaster(channel, servers, listElem, theList);
                    int ls = 0;
                    synchronized (servers) {
                        servers.add(s);
                        ls = theList.size();
                        s.start();
                    }
                    if (ls > 0) {
                        logger.info("sending " + ls + " list elements");
                        synchronized (theList) {
                            Iterator it = theList.entrySet().iterator();
                            for (int i = 0; i < ls; i++) {
                                e = (Entry) it.next();
                                n = e.getKey();
                                o = e.getValue();
                                try {
                                    s.sendChannel(n, o);
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
    }
