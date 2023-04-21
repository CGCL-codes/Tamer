    public void run() {
        logger.info("reducer server running with " + cf);
        SocketChannel channel = null;
        try {
            channel = cf.getChannel();
            pairChannel = new TaggedSocketChannel(channel);
            pairChannel.init();
        } catch (InterruptedException e) {
            logger.debug("get pair channel interrupted");
            e.printStackTrace();
            return;
        }
        if (debug) {
            logger.info("pairChannel   = " + pairChannel);
        }
        finner.initIdle(threadsPerNode);
        AtomicInteger active = new AtomicInteger(0);
        HybridReducerReceiver<C> receiver = new HybridReducerReceiver<C>(threadsPerNode, finner, active, pairChannel, theList, pairlist);
        receiver.start();
        Pair<C> pair;
        boolean set = false;
        boolean goon = true;
        int polIndex = -1;
        int red = 0;
        int sleeps = 0;
        while (goon) {
            logger.debug("receive request");
            Object req = null;
            try {
                req = pairChannel.receive(pairTag);
            } catch (InterruptedException e) {
                goon = false;
                e.printStackTrace();
            } catch (IOException e) {
                goon = false;
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                goon = false;
                e.printStackTrace();
            }
            logger.info("received request, req = " + req);
            if (req == null) {
                goon = false;
                break;
            }
            if (!(req instanceof GBTransportMessReq)) {
                goon = false;
                break;
            }
            logger.info("find pair");
            while (!pairlist.hasNext()) {
                if (!finner.hasJobs() && !pairlist.hasNext()) {
                    goon = false;
                    break;
                }
                try {
                    sleeps++;
                    logger.info("waiting for reducers, remaining = " + finner.getJobs());
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    goon = false;
                    break;
                }
            }
            if (!pairlist.hasNext() && !finner.hasJobs()) {
                logger.info("termination detection: no pairs and no jobs left");
                goon = false;
                break;
            }
            finner.notIdle();
            pair = pairlist.removeNext();
            if (debug) {
                logger.info("active count = " + active.get());
                logger.info("send pair = " + pair);
            }
            GBTransportMess msg = null;
            if (pair != null) {
                msg = new GBTransportMessPairIndex(pair);
            } else {
                msg = new GBTransportMess();
            }
            try {
                red++;
                pairChannel.send(pairTag, msg);
                int a = active.getAndIncrement();
            } catch (IOException e) {
                e.printStackTrace();
                goon = false;
                break;
            }
        }
        logger.info("terminated, send " + red + " reduction pairs");
        logger.debug("send end");
        try {
            for (int i = 0; i < threadsPerNode; i++) {
                pairChannel.send(pairTag, new GBTransportMessEnd());
            }
            pairChannel.send(resultTag, new GBTransportMessEnd());
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
        receiver.terminate();
        int d = active.get();
        logger.info("remaining active tasks = " + d);
        pairChannel.close();
        logger.info("redServ pairChannel.close()");
        finner.release();
        channel.close();
        logger.info("redServ channel.close()");
    }
