    @Before
    public void setUp() throws Exception {
        AudioFormat pcma = FormatFactory.createAudioFormat("pcma", 8000, 8, 1);
        Formats fmts = new Formats();
        fmts.add(pcma);
        Formats dstFormats = new Formats();
        dstFormats.add(FormatFactory.createAudioFormat("LINEAR", 8000, 16, 1));
        dspFactory.addCodec("org.mobicents.media.server.impl.dsp.audio.g711.alaw.Encoder");
        dspFactory.addCodec("org.mobicents.media.server.impl.dsp.audio.g711.alaw.Decoder");
        dsp11 = dspFactory.newProcessor();
        dsp12 = dspFactory.newProcessor();
        dsp21 = dspFactory.newProcessor();
        dsp22 = dspFactory.newProcessor();
        clock = new DefaultClock();
        scheduler = new Scheduler(4);
        scheduler.setClock(clock);
        scheduler.start();
        udpManager = new UdpManager(scheduler);
        udpManager.start();
        rtpManager = new RTPManager(udpManager);
        rtpManager.setBindAddress("127.0.0.1");
        rtpManager.setScheduler(scheduler);
        detector = new DetectorImpl("dtmf", scheduler);
        detector.setVolume(-35);
        detector.setDuration(40);
        detector.addListener(this);
        detector.setDsp(dsp11);
        detector.setFormats(dstFormats);
        channel = rtpManager.getChannel();
        channel.bind();
        sender = new Sender(channel.getLocalPort());
        channel.setPeer(new InetSocketAddress("127.0.0.1", 9200));
        channel.setFormatMap(AVProfile.audio);
        txPipe = new PipeImpl();
        rxPipe = new PipeImpl();
        rxPipe.connect(detector);
        rxPipe.connect(channel.getInput());
    }
