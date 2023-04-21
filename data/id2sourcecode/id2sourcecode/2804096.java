    @Override
    public void updateConnections() {
        int[] currV = manager.getCurrentViewProcesses();
        try {
            Mac macDummy = Mac.getInstance(manager.getStaticConf().getHmacAlgorithm());
            for (int i = 0; i < currV.length; i++) {
                rl.readLock().lock();
                if (sessionTable.get(currV[i]) == null) {
                    rl.readLock().unlock();
                    rl.writeLock().lock();
                    try {
                        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
                        bootstrap.setOption("tcpNoDelay", true);
                        bootstrap.setOption("keepAlive", true);
                        bootstrap.setOption("connectTimeoutMillis", 10000);
                        bootstrap.setPipelineFactory(new NettyClientPipelineFactory(this, true, sessionTable, authKey, macDummy.getMacLength(), manager, rl, signatureLength, new ReentrantLock()));
                        ChannelFuture future = bootstrap.connect(manager.getRemoteAddress(currV[i]));
                        Mac macSend = Mac.getInstance(manager.getStaticConf().getHmacAlgorithm());
                        macSend.init(authKey);
                        Mac macReceive = Mac.getInstance(manager.getStaticConf().getHmacAlgorithm());
                        macReceive.init(authKey);
                        NettyClientServerSession cs = new NettyClientServerSession(future.getChannel(), macSend, macReceive, currV[i], manager.getStaticConf().getRSAPublicKey(currV[i]), new ReentrantLock());
                        sessionTable.put(currV[i], cs);
                        System.out.println("Connecting to replica " + currV[i] + " at " + manager.getRemoteAddress(currV[i]));
                        future.awaitUninterruptibly();
                    } catch (InvalidKeyException ex) {
                        ex.printStackTrace();
                    }
                    rl.writeLock().unlock();
                } else {
                    rl.readLock().unlock();
                }
            }
        } catch (NoSuchAlgorithmException ex) {
        }
    }
