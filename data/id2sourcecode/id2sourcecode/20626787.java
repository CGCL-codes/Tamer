    public void run() {
        logger.debug("reducer server running");
        try {
            pairChannel = cf.getChannel();
        } catch (InterruptedException e) {
            logger.debug("get pair channel interrupted");
            e.printStackTrace();
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("pairChannel = " + pairChannel);
        }
        CriticalPair<C> pair;
        GenPolynomial<C> H = null;
        boolean set = false;
        boolean goon = true;
        int polIndex = -1;
        int red = 0;
        int sleeps = 0;
        while (goon) {
            logger.debug("receive request");
            Object req = null;
            try {
                req = pairChannel.receive();
            } catch (IOException e) {
                goon = false;
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                goon = false;
                e.printStackTrace();
            }
            if (req == null) {
                goon = false;
                break;
            }
            if (!(req instanceof GBSPTransportMessReq)) {
                goon = false;
                break;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("find pair");
                logger.debug("pool.hasJobs() " + pool.hasJobs() + " pairlist.hasNext() " + pairlist.hasNext());
            }
            while (!pairlist.hasNext()) {
                pairlist.update();
                if (!set) {
                    pool.beIdle();
                    set = true;
                }
                if (!pool.hasJobs() && !pairlist.hasNext()) {
                    goon = false;
                    break;
                }
                try {
                    sleeps++;
                    if (sleeps % 10 == 0) {
                        logger.info(" reducer is sleeping");
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    goon = false;
                    break;
                }
            }
            if (!pairlist.hasNext() && !pool.hasJobs()) {
                goon = false;
                break;
            }
            if (set) {
                set = false;
                pool.notIdle();
            }
            pair = pairlist.getNext();
            if (logger.isDebugEnabled()) {
                logger.debug("send pair = " + pair);
                logger.info("theList keys " + theList.keySet());
            }
            if (logger.isDebugEnabled()) {
                logger.info("inWork " + pairlist.inWork());
            }
            GBSPTransportMess msg = null;
            if (pair != null) {
                msg = new GBSPTransportMessPairIndex(pair);
            } else {
                msg = new GBSPTransportMess();
            }
            try {
                pairChannel.send(msg);
            } catch (IOException e) {
                e.printStackTrace();
                goon = false;
                break;
            }
            pairlist.update();
            Object rh = null;
            try {
                rh = pairChannel.receive();
            } catch (IOException e) {
                e.printStackTrace();
                goon = false;
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                goon = false;
                break;
            }
            if (logger.isDebugEnabled()) {
                logger.info("received H polynomial rh = " + rh);
            }
            if (rh == null) {
                if (pair != null) {
                    polIndex = pairlist.record(pair, null);
                }
                pairlist.update();
            } else if (rh instanceof GBSPTransportMessPoly) {
                red++;
                H = ((GBSPTransportMessPoly<C>) rh).pol;
                if (H == null) {
                    if (pair != null) {
                        polIndex = pairlist.record(pair, H);
                    }
                    pairlist.update();
                } else {
                    if (H.isZERO()) {
                        polIndex = pairlist.record(pair, H);
                    } else {
                        if (H.isONE()) {
                            pairlist.putOne();
                            theList.put(new Integer(0), H);
                            goon = false;
                        } else {
                            polIndex = pairlist.record(pair, H);
                            theList.put(new Integer(polIndex), H);
                        }
                    }
                }
            } else {
                if (pair != null) {
                    polIndex = pairlist.record(pair, null);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("invalid message " + rh);
                }
            }
        }
        logger.info("terminated, done " + red + " reductions");
        logger.debug("send end");
        try {
            pairChannel.send(new GBSPTransportMessEnd());
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
        pool.beIdle();
        pairChannel.close();
    }
