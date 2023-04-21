    @Override
    public void run() {
        try {
            cwi = new ChannelWorldInterfaceImpl(this);
            wci = worldRegistry.registerChannelServer(key, cwi);
            props = wci.getGameProperties();
            expRate = Integer.parseInt(props.getProperty("net.sf.odinms.world.exp"));
            mesoRate = Integer.parseInt(props.getProperty("net.sf.odinms.world.meso"));
            dropRate = Integer.parseInt(props.getProperty("net.sf.odinms.world.drop"));
            bossdropRate = Integer.parseInt(props.getProperty("net.sf.odinms.world.bossdrop"));
            petExpRate = Integer.parseInt(props.getProperty("net.sf.odinms.world.petExp"));
            mountExpRate = Integer.parseInt(props.getProperty("net.sf.odinms.world.mountExp"));
            shopMesoRate = Integer.parseInt(props.getProperty("net.sf.odinms.world.shopMesoRate"));
            serverMessage = props.getProperty("net.sf.odinms.world.serverMessage");
            dropUndroppables = Boolean.parseBoolean(props.getProperty("net.sf.odinms.world.alldrop", "false"));
            orbgain = Boolean.parseBoolean(props.getProperty("net.sf.odinms.world.orbgain", "false"));
            moreThanOne = Boolean.parseBoolean(props.getProperty("net.sf.odinms.world.morethanone", "false"));
            eventSM = new EventScriptManager(this, props.getProperty("net.sf.odinms.channel.events").split(","));
            gmWhiteText = Boolean.parseBoolean(props.getProperty("net.sf.odinms.world.gmWhiteText", "false"));
            cashshop = Boolean.parseBoolean(props.getProperty("net.sf.odinms.world.cashshop", "false"));
            mts = Boolean.parseBoolean(props.getProperty("net.sf.odinms.world.mts", "false"));
            Properties dbProp = new Properties();
            FileReader fileReader = new FileReader("db.properties");
            dbProp.load(fileReader);
            fileReader.close();
            DatabaseConnection.setProps(dbProp);
            DatabaseConnection.getConnection();
            Connection c = DatabaseConnection.getConnection();
            PreparedStatement ps;
            try {
                ps = c.prepareStatement("UPDATE accounts SET loggedin = 0");
                ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                log.error("Could not reset databases", ex);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        port = Integer.parseInt(props.getProperty("net.sf.odinms.channel.net.port"));
        ip = props.getProperty("net.sf.odinms.channel.net.interface") + ":" + port;
        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
        acceptor = new SocketAcceptor();
        SocketAcceptorConfig cfg = new SocketAcceptorConfig();
        cfg.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MapleCodecFactory()));
        TimerManager tMan = TimerManager.getInstance();
        tMan.start();
        tMan.register(AutobanManager.getInstance(), 60000);
        try {
            MapleServerHandler serverHandler = new MapleServerHandler(PacketProcessor.getProcessor(PacketProcessor.Mode.CHANNELSERVER), channel);
            acceptor.bind(new InetSocketAddress(port), serverHandler, cfg);
            log.info("Channel {}: Listening on port {}", getChannel(), port);
            wci.serverReady();
            eventSM.init();
        } catch (IOException e) {
            log.error("Binding to port " + port + " failed (ch: " + getChannel() + ")", e);
        }
    }
