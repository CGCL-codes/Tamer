    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        if (this.closed) {
            return;
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
        }
        rl.writeLock().lock();
        ArrayList<NettyClientServerSession> sessions = new ArrayList<NettyClientServerSession>(sessionTable.values());
        for (NettyClientServerSession ncss : sessions) {
            if (ncss.getChannel() == ctx.getChannel()) {
                try {
                    Mac macDummy = Mac.getInstance(manager.getStaticConf().getHmacAlgorithm());
                    ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
                    bootstrap.setPipelineFactory(new NettyClientPipelineFactory(this, true, sessionTable, authKey, macDummy.getMacLength(), manager, rl, TOMUtil.getSignatureSize(manager), new ReentrantLock()));
                    if (manager.getRemoteAddress(ncss.getReplicaId()) != null) {
                        ChannelFuture future = bootstrap.connect(manager.getRemoteAddress(ncss.getReplicaId()));
                        Mac macSend = ncss.getMacSend();
                        Mac macReceive = ncss.getMacReceive();
                        NettyClientServerSession cs = new NettyClientServerSession(future.getChannel(), macSend, macReceive, ncss.getReplicaId(), manager.getStaticConf().getRSAPublicKey(ncss.getReplicaId()), new ReentrantLock());
                        sessionTable.remove(ncss.getReplicaId());
                        sessionTable.put(ncss.getReplicaId(), cs);
                    } else {
                        sessionTable.remove(ncss.getReplicaId());
                    }
                } catch (NoSuchAlgorithmException ex) {
                }
            }
        }
        rl.writeLock().unlock();
    }
