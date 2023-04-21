    public void clientPart(String host) throws IOException {
        ChannelFactory cf = new ChannelFactory(port + 10);
        cf.init();
        SocketChannel pairChannel = cf.getChannel(host, port);
        final int DL_PORT = port + 100;
        DistHashTable<Integer, GenPolynomial<C>> theList = new DistHashTable<Integer, GenPolynomial<C>>(host, DL_PORT);
        ReducerClientSeqPair<C> R = new ReducerClientSeqPair<C>(pairChannel, theList);
        R.run();
        pairChannel.close();
        theList.terminate();
        cf.terminate();
        return;
    }
