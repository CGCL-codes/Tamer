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
        Pair<C> pair;
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
            if (!(req instanceof GBTransportMessReq)) {
                goon = false;
                break;
            }
            logger.debug("find pair");
            while (!pairlist.hasNext()) {
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
            pair = pairlist.removeNext();
            logger.debug("send pair = " + pair);
            GBTransportMess msg = null;
            if (pair != null) {
                msg = new GBTransportMessPairIndex(pair);
            } else {
                msg = new GBTransportMess();
            }
            try {
                pairChannel.send(msg);
            } catch (IOException e) {
                e.printStackTrace();
                goon = false;
                break;
            }
            logger.debug("#distributed list = " + theList.size());
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
            if (rh == null) {
                if (pair != null) {
                    pair.setZero();
                }
            } else if (rh instanceof GBTransportMessPoly) {
                red++;
                H = ((GBTransportMessPoly<C>) rh).pol;
                if (logger.isDebugEnabled()) {
                    logger.debug("H = " + H);
                }
                if (H == null) {
                    if (pair != null) {
                        pair.setZero();
                    }
                } else {
                    if (H.isZERO()) {
                        pair.setZero();
                    } else {
                        if (H.isONE()) {
                            polIndex = pairlist.putOne();
                            GenPolynomial<C> nn = theList.put(new Integer(polIndex), H);
                            if (nn != null) {
                                logger.info("double polynomials nn = " + nn + ", H = " + H);
                            }
                            goon = false;
                            break;
                        } else {
                            polIndex = pairlist.put(H);
                            GenPolynomial<C> nn = theList.put(new Integer(polIndex), H);
                            if (nn != null) {
                                logger.info("double polynomials nn = " + nn + ", H = " + H);
                            }
                        }
                    }
                }
            }
        }
        logger.info("terminated, done " + red + " reductions");
        logger.debug("send end");
        try {
            pairChannel.send(new GBTransportMessEnd());
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
        pool.beIdle();
        pairChannel.close();
    }
