    public void clientPart(String host) throws IOException {
        ChannelFactory cf = new ChannelFactory(port + 10);
        cf.init();
        SocketChannel channel = cf.getChannel(host, port);
        TaggedSocketChannel pairChannel = new TaggedSocketChannel(channel);
        pairChannel.init();
        if (debug) {
            logger.info("clientPart pairChannel   = " + pairChannel);
        }
        final int DL_PORT = port + 100;
        DistHashTable<Integer, GenPolynomial<C>> theList = new DistHashTable<Integer, GenPolynomial<C>>(host, DL_PORT);
        ThreadPool pool = new ThreadPool(threadsPerNode);
        logger.info("client using pool = " + pool);
        for (int i = 0; i < threadsPerNode; i++) {
            HybridReducerClient<C> Rr = new HybridReducerClient<C>(threadsPerNode, pairChannel, i, theList);
            pool.addJob(Rr);
        }
        if (debug) {
            logger.info("clients submitted");
        }
        pool.terminate();
        logger.info("client pool.terminate()");
        pairChannel.close();
        logger.info("client pairChannel.close()");
        theList.terminate();
        cf.terminate();
        logger.info("client cf.terminate()");
        channel.close();
        logger.info("client channel.close()");
        return;
    }
