    public NettyClientServerCommunicationSystemClientSide(ClientViewManager manager) {
        super();
        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            PBEKeySpec spec = new PBEKeySpec(PASSWORD.toCharArray());
            authKey = fac.generateSecret(spec);
            this.manager = manager;
            this.rl = new ReentrantReadWriteLock();
            Mac macDummy = Mac.getInstance(manager.getStaticConf().getHmacAlgorithm());
            signatureLength = TOMUtil.getSignatureSize(manager);
            int[] currV = manager.getCurrentViewProcesses();
            for (int i = 0; i < currV.length; i++) {
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
                    if (!future.isSuccess()) {
                        System.err.println("Impossible to connect to " + currV[i]);
                    }
                } catch (java.lang.NullPointerException ex) {
                    System.err.println("Should fix the problem, and I think it has no other implications :-), " + "but we must make the servers store the view in a different place.");
                } catch (InvalidKeyException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        } catch (InvalidKeySpecException ex) {
            ex.printStackTrace(System.err);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace(System.err);
        }
    }
